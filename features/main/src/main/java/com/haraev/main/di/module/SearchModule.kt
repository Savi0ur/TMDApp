package com.haraev.main.di.module

import com.haraev.core.common.ThreadScheduler
import com.haraev.main.data.SearchRepositoryImpl
import com.haraev.main.data.api.MainService
import com.haraev.main.domain.repository.SearchRepository
import com.haraev.main.domain.usecase.SearchUseCase
import com.haraev.main.presentation.search.SearchViewModelFactory
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

    @Provides
    fun provideSearchViewModelFactory(
        searchUseCase: SearchUseCase,
        threadScheduler: ThreadScheduler
    ) : SearchViewModelFactory {
        return SearchViewModelFactory(
            searchUseCase,
            threadScheduler
        )
    }

}