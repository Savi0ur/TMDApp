package com.haraev.main.presentation.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.haraev.core.common.ThreadScheduler
import com.haraev.main.domain.usecase.SearchUseCase
import javax.inject.Inject

class SearchViewModelFactory @Inject constructor(
    private val searchUseCase: SearchUseCase,
    private val scheduler: ThreadScheduler
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass != SearchViewModel::class.java) {
            throw IllegalArgumentException("Unknown ViewModel class")
        }
        return SearchViewModel(searchUseCase, scheduler) as T
    }
}