package com.finter.india.persistence

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.finter.india.model.Pokemon
import com.finter.india.model.PokemonInfo
import com.finter.india.persistence.converters.InfoTypeConverter
import com.finter.india.persistence.converters.InfoTypeResponseConverter

@Database(entities = [Pokemon::class, PokemonInfo::class], version = 1, exportSchema = true)
@TypeConverters(value = [InfoTypeConverter::class, InfoTypeResponseConverter::class])
abstract class AppDatabase : RoomDatabase() {

    abstract fun pokemonDao(): PokemonDao
    abstract fun pokemonInfoDao(): PokemonInfoDao
}
