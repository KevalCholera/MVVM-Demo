package com.finter.india.persistence.converters

import androidx.room.TypeConverter
import com.finter.india.model.PokemonInfo
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types

open class InfoTypeResponseConverter {

    private val moshi = Moshi.Builder().build()

    @TypeConverter
    fun fromString(value: String): List<PokemonInfo.TypeResponse>? {
        val listType =
            Types.newParameterizedType(List::class.java, PokemonInfo.TypeResponse::class.java)
        val adapter: JsonAdapter<List<PokemonInfo.TypeResponse>> = moshi.adapter(listType)
        return adapter.fromJson(value)
    }

    @TypeConverter
    fun fromInfoType(type: List<PokemonInfo.TypeResponse>?): String {
        val listType =
            Types.newParameterizedType(List::class.java, PokemonInfo.TypeResponse::class.java)
        val adapter: JsonAdapter<List<PokemonInfo.TypeResponse>> = moshi.adapter(listType)
        return adapter.toJson(type)
    }
}
