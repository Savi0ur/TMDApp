package com.haraev.main.presentation.search

import androidx.lifecycle.MutableLiveData
import com.haraev.core.aac.delegate
import com.haraev.core.common.ThreadScheduler
import com.haraev.core.common.scheduleIoToUi
import com.haraev.core.ui.BaseViewModel
import com.haraev.main.R
import com.haraev.main.data.model.response.MovieDetailsResponse
import com.haraev.main.domain.usecase.SearchUseCase
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import timber.log.Timber
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class SearchViewModel @Inject constructor(
    private val searchUseCase: SearchUseCase,
    private val threadScheduler: ThreadScheduler
) : BaseViewModel() {

    val uiState = MutableLiveData<SearchViewState>(createInitialState())
    private var state: SearchViewState by uiState.delegate()

    private var searchDisposables = CompositeDisposable()

    fun onSearchEditTextTextChanged(observable: Observable<String>) {
        observable
            .debounce(300, TimeUnit.MILLISECONDS)
            .distinctUntilChanged()
            .filter { it.isNotBlank() }
            .scheduleIoToUi(threadScheduler)
            .subscribe({
                stopSearchProcess()
                changeProgressBarState(true)
                searchMovies(it)
            }, {
                Timber.tag(TAG).e(it)
                showErrorMessage(R.string.unknown_error_message)
            })
            .autoDispose()
    }

    fun onClearButtonClicked() {
        stopSearchProcess()
        showMovies(null)
    }

    private fun stopSearchProcess() {
        searchDisposables.clear()
        changeProgressBarState(false)
    }

    private fun searchMovies(query: String) {
        searchDisposables.add(
            searchUseCase.getMovies(query, 1)
                .scheduleIoToUi(threadScheduler)
                .subscribe({ list ->
                    changeProgressBarState(false)
                    showMovies(list)
                }, { e ->
                    changeProgressBarState(false)
                    Timber.tag(TAG).e(e)
                    showErrorMessage(R.string.unknown_error_message)
                })
                .autoDispose()
        )
    }

    private fun showMovies(movies: List<MovieDetailsResponse>?) {
        state = state.copy(movies = movies)
    }

    private fun changeProgressBarState(visible: Boolean) {
        state = state.copy(progressBarVisibility = visible)
    }

    private fun createInitialState(): SearchViewState =
        SearchViewState(movies = null)

    companion object {
        private const val TAG = "SearchViewModel"
    }
}