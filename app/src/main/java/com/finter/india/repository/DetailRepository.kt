package com.finter.india.repository

import androidx.lifecycle.MutableLiveData
import com.finter.india.model.PokemonInfo
import com.finter.india.network.PokedexClient
import com.finter.india.persistence.PokemonInfoDao
import com.skydoves.sandwich.*
import com.skydoves.whatif.whatIfNotNull
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class DetailRepository @Inject constructor(
    private val pokedexClient: PokedexClient,
    private val pokemonInfoDao: PokemonInfoDao
) : Repository {

    suspend fun fetchPokemonInfo(
        name: String,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) = withContext(Dispatchers.IO) {
        val liveData = MutableLiveData<PokemonInfo>()
        val pokemonInfo = pokemonInfoDao.getPokemonInfo(name)
        if (pokemonInfo == null) {
            pokedexClient.fetchPokemonInfo(name = name) {
                it.onSuccess {
                    data.whatIfNotNull { response ->
                        liveData.postValue(response)
                        pokemonInfoDao.insertPokemonInfo(response)
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
        liveData.apply { postValue(pokemonInfo) }
    }
}
