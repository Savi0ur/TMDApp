package com.haraev.main.presentation.search

import androidx.lifecycle.MutableLiveData
import com.haraev.core.aac.EventsQueue
import com.haraev.core.aac.delegate
import com.haraev.core.common.ThreadScheduler
import com.haraev.core.common.scheduleIoToUi
import com.haraev.core.ui.BaseViewModel
import com.haraev.main.R
import com.haraev.main.data.model.MovieUi
import com.haraev.main.domain.usecase.SearchUseCase
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
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

    private var lastQuery = ""

    fun onSearchEditTextTextChanged(observable: Observable<String>) {
        observable.debounce(300, TimeUnit.MILLISECONDS)
            .scheduleIoToUi(threadScheduler)
            .subscribe({
                if (it.isNotBlank() && it != lastQuery) {
                    changeProgressBarState(true)
                    stopSearchProcess()
                    searchMovie(it)
                    lastQuery = it
                }
            }, {
                Timber.tag(TAG).e(it)
                showErrorMessage(R.string.unknown_error_message)
            })
            .autoDispose()
    }

    fun stopSearchProcess() {
        searchDisposables.clear()
    }

    @Suppress("UnstableApiUsage")
    private fun searchMovie(query: String) {
        searchDisposables.add(searchUseCase.getMovies(query, 1)
            .scheduleIoToUi(threadScheduler)
            .doOnTerminate {
                changeProgressBarState(false)
            }
            .subscribe({ movies ->
                showMovies(movies.sortedByDescending { it.voteAverage })
            }, {
                Timber.tag(TAG).e(it)
                showErrorMessage(R.string.unknown_error_message)
            })
            .autoDispose()
        )
    }

    private fun showMovies(movies: List<MovieUi>) {
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