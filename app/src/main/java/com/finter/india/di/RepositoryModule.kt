package com.finter.india.di

import com.finter.india.network.PokedexClient
import com.finter.india.persistence.PokemonDao
import com.finter.india.persistence.PokemonInfoDao
import com.finter.india.repository.DetailRepository
import com.finter.india.repository.MainRepository
import com.finter.india.repository.employee.add.EmployeeAddRepository
import com.finter.india.repository.employee.add.EmployeeRemoveAttachmentRepository
import com.finter.india.repository.employee.delete.EmployeeDeleteRepository
import com.finter.india.repository.employee.details.EmployeeDetailsRepository
import com.finter.india.repository.employee.list.EmployeeRepository
import com.finter.india.repository.employee.update.EmployeeUpdateRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent
import dagger.hilt.android.scopes.ActivityRetainedScoped

@Module
@InstallIn(ActivityRetainedComponent::class)
object RepositoryModule {

    @Provides
    @ActivityRetainedScoped
    fun provideMainRepository(
        pokedexClient: PokedexClient,
        pokemonDao: PokemonDao
    ): MainRepository {
        return MainRepository(pokedexClient, pokemonDao)
    }

    @Provides
    @ActivityRetainedScoped
    fun provideDetailRepository(
        pokedexClient: PokedexClient,
        pokemonInfoDao: PokemonInfoDao
    ): DetailRepository {
        return DetailRepository(pokedexClient, pokemonInfoDao)
    }

    @Provides
    @ActivityRetainedScoped
    fun provideEmployeeRepository(
        pokedexClient: PokedexClient
    ): EmployeeRepository {
        return EmployeeRepository(
            pokedexClient
        )
    }

    @Provides
    @ActivityRetainedScoped
    fun provideEmployeeAddRepository(
        pokedexClient: PokedexClient
    ): EmployeeAddRepository {
        return EmployeeAddRepository(
            pokedexClient
        )
    }

    @Provides
    @ActivityRetainedScoped
    fun provideEmployeeDetailsRepository(
        pokedexClient: PokedexClient
    ): EmployeeDetailsRepository {
        return EmployeeDetailsRepository(
            pokedexClient
        )
    }

    @Provides
    @ActivityRetainedScoped
    fun provideEmployeeDeleteRepository(
        pokedexClient: PokedexClient
    ): EmployeeDeleteRepository {
        return EmployeeDeleteRepository(
            pokedexClient
        )
    }

    @Provides
    @ActivityRetainedScoped
    fun provideEmployeeUpdateRepository(
        pokedexClient: PokedexClient
    ): EmployeeUpdateRepository {
        return EmployeeUpdateRepository(
            pokedexClient
        )
    }

    @Provides
    @ActivityRetainedScoped
    fun provideEmployeeRemoveAttachmentRepository(
        pokedexClient: PokedexClient
    ): EmployeeRemoveAttachmentRepository {
        return EmployeeRemoveAttachmentRepository(
            pokedexClient
        )
    }
}