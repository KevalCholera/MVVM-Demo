package com.finter.india.di

import android.app.Application
import androidx.room.Room
import com.finter.india.persistence.AppDatabase
import com.finter.india.persistence.PokemonDao
import com.finter.india.persistence.PokemonInfoDao
import com.finter.india.utils.SharedPreferenceUtil
import com.finter.india.utils.SharedPreferenceUtilC
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import javax.inject.Singleton

@Module
@InstallIn(ApplicationComponent::class)
object PersistenceModule {

    @Provides
    @Singleton
    fun provideAppDatabase(application: Application): AppDatabase {
        return Room
            .databaseBuilder(application, AppDatabase::class.java, "Pokedex.db")
            .fallbackToDestructiveMigration()
            .allowMainThreadQueries()
            .build()
    }

    @Provides
    @Singleton
    fun providePokemonDao(appDatabase: AppDatabase): PokemonDao {
        return appDatabase.pokemonDao()
    }

    @Provides
    @Singleton
    fun providePokemonInfoDao(appDatabase: AppDatabase): PokemonInfoDao {
        return appDatabase.pokemonInfoDao()
    }

    @Provides
    @Singleton
    fun provideSharedPreferences(application: Application): SharedPreferenceUtil =
        SharedPreferenceUtilC(application.applicationContext)
}
