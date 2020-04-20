package com.haraev.main.di.module.favorite

import com.haraev.core.data.LocalUserDataSource
import com.haraev.database.Database
import com.haraev.main.data.api.MainService
import com.haraev.main.data.FavoriteRepositoryImpl
import com.haraev.main.domain.repository.FavoriteRepository
import com.haraev.main.domain.usecase.FavoriteUseCase
import dagger.Module
import dagger.Provides

@Module
class FavoriteModule {

    @Provides
    fun provideFavoriteRepository(
        mainService: MainService,
        localUserDataSource: LocalUserDataSource,
        database: Database
    ): FavoriteRepository {
        return FavoriteRepositoryImpl(
            mainService,
            localUserDataSource,
            database
        )
    }

    @Provides
    fun provideFavoriteUseCase(
        favoriteRepository: FavoriteRepository
    ): FavoriteUseCase {
        return FavoriteUseCase(favoriteRepository)
    }
}