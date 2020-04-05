package com.haraev.main.di.module.search

import com.haraev.main.data.SearchRepositoryImpl
import com.haraev.main.data.api.MainService
import com.haraev.main.domain.repository.SearchRepository
import com.haraev.main.domain.usecase.SearchUseCase
import dagger.Module
import dagger.Provides

@Module
class SearchModule {

    @Provides
    fun provideSearchRepository(
        mainService: MainService
    ) : SearchRepository {
        return SearchRepositoryImpl(
            mainService
        )
    }

    @Provides
    fun provideSearchUseCase(
        searchRepository: SearchRepository
    ) : SearchUseCase {
        return SearchUseCase(
            searchRepository
        )
    }

}