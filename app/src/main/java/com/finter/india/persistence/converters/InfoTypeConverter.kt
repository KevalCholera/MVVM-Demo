package com.finter.india.persistence.converters

import androidx.room.TypeConverter
import com.finter.india.model.PokemonInfo
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi

open class InfoTypeConverter {

    private val moshi = Moshi.Builder().build()

    @TypeConverter
    fun fromString(value: String): PokemonInfo.Type? {
        val adapter: JsonAdapter<PokemonInfo.Type> = moshi.adapter(PokemonInfo.Type::class.java)
        return adapter.fromJson(value)
    }

    @TypeConverter
    fun fromInfoType(type: PokemonInfo.Type): String {
        val adapter: JsonAdapter<PokemonInfo.Type> = moshi.adapter(PokemonInfo.Type::class.java)
        return adapter.toJson(type)
    }
}
