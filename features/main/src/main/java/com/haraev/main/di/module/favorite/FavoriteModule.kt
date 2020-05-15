package com.haraev.main.di.module.favorite

import com.haraev.core.data.LocalUserDataSource
import com.haraev.main.data.api.MainService
import com.haraev.main.data.FavoriteRepositoryImpl
import com.haraev.main.data.MovieRepositoryImpl
import com.haraev.main.domain.repository.FavoriteRepository
import com.haraev.main.domain.repository.MovieRepository
import com.haraev.main.domain.usecase.FavoriteUseCase
import dagger.Module
import dagger.Provides

@Module
class FavoriteModule {

    @Provides
    fun provideFavoriteRepository(
        mainService: MainService,
        localUserDataSource: LocalUserDataSource
    ): FavoriteRepository {
        return FavoriteRepositoryImpl(
            mainService,
            localUserDataSource
        )
    }

    @Provides
    fun provideFavoriteUseCase(
        favoriteRepository: FavoriteRepository
    ): FavoriteUseCase {
        return FavoriteUseCase(favoriteRepository)
    }
}