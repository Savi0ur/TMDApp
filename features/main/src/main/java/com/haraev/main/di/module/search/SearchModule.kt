package com.haraev.main.di.module.search

import com.haraev.main.data.MovieRepositoryImpl
import com.haraev.main.data.api.MainService
import com.haraev.main.domain.repository.MovieRepository
import com.haraev.main.domain.usecase.SearchUseCase
import dagger.Module
import dagger.Provides

@Module
class SearchModule {

    @Provides
    fun provideSearchRepository(
        mainService: MainService
    ): MovieRepository {
        return MovieRepositoryImpl(mainService)
    }

    @Provides
    fun provideSearchUseCase(
        movieRepository: MovieRepository
    ): SearchUseCase {
        return SearchUseCase(movieRepository)
    }

}