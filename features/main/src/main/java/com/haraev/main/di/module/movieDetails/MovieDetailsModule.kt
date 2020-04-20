package com.haraev.main.di.module.movieDetails

import com.haraev.core.data.LocalUserDataSource
import com.haraev.database.Database
import com.haraev.main.data.MovieDetailsRepositoryImpl
import com.haraev.main.data.api.MainService
import com.haraev.main.domain.repository.MovieDetailsRepository
import com.haraev.main.domain.usecase.MovieDetailsUseCase
import dagger.Module
import dagger.Provides

@Module
class MovieDetailsModule {

    @Provides
    fun provideMovieDetailsRepository(
        mainService: MainService,
        localUserDataSource: LocalUserDataSource,
        database: Database
    ) : MovieDetailsRepository {
        return MovieDetailsRepositoryImpl(
            mainService,
            localUserDataSource,
            database
        )
    }

    @Provides
    fun provideMovieDetailsUseCase(
        movieDetailsRepository: MovieDetailsRepository
    ) : MovieDetailsUseCase {
        return MovieDetailsUseCase(movieDetailsRepository)
    }
}