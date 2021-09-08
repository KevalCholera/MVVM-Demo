package com.finter.india.repository

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.finter.india.model.Pokemon
import com.finter.india.network.PokedexClient
import com.finter.india.persistence.PokemonDao
import com.skydoves.sandwich.*
import com.skydoves.whatif.whatIfNotNull
import javax.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import timber.log.Timber

class MainRepository @Inject constructor(
    private val pokedexClient: PokedexClient,
    private val pokemonDao: PokemonDao
) : Repository {

    suspend fun fetchPokemonList(
        page: Int,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) = withContext(Dispatchers.IO) {
        val liveData = MutableLiveData<List<Pokemon>>()
        var pokemonList = pokemonDao.getPokemonList(page)
        if (pokemonList.isEmpty()) {
            pokedexClient.fetchPokemonList(page = page) {
                // handle the case when the API request gets a success response.
                it.onSuccess {
                    data.whatIfNotNull { response ->
                        pokemonList = response.results
                        pokemonList.forEach { pokemon -> pokemon.page = page }
                        liveData.postValue(pokemonList)
                        pokemonDao.insertPokemonList(pokemonList)
                        onSuccess()
                    }
                }
                    // handle the case when the API request gets a error response.
                    // e.g. internal server error.
                    .onError {
                     if (statusCode == com.skydoves.sandwich.StatusCode.Forbidden) {
                        onError(statusCode.name)
                    } else {
                        onError(message())
                    }
                }
                    // handle the case when the API request gets a exception response.
                    // e.g. network connection error.
                    .onException {
                        onError(message())
                    }
            }
        } else {
            onSuccess()
        }
        liveData.apply { postValue(pokemonList) }
    }
}
