package com.haraev.main.presentation.search

import androidx.lifecycle.MutableLiveData
import com.haraev.core.aac.EventsQueue
import com.haraev.core.aac.delegate
import com.haraev.core.common.ThreadScheduler
import com.haraev.core.common.scheduleIoToUi
import com.haraev.core.ui.BaseViewModel
import com.haraev.main.R
import com.haraev.main.data.model.Movie
import com.haraev.main.domain.usecase.SearchUseCase
import io.reactivex.Observable
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class SearchViewModel @Inject constructor(
    private val searchUseCase: SearchUseCase,
    private val threadScheduler: ThreadScheduler
) : BaseViewModel() {

    val uiState = MutableLiveData<SearchViewState>(createInitialState())
    private var state: SearchViewState by uiState.delegate()

    val eventsQueue = EventsQueue()

    fun onSearchEditTextTextChanged(observable: Observable<String>) {
        observable
            .debounce(300, TimeUnit.MILLISECONDS)
            .scheduleIoToUi(threadScheduler)
            .subscribe({
                if (it.isBlank()) {
                    showDefaultImage()
                } else {
                    searchMovie(it)
                }
            }, {
                eventsQueue.offer(SearchEvents.ErrorMessage(R.string.unknown_error_message))
            })
            .autoDispose()

    }

    private fun searchMovie(query: String) {
        searchUseCase.getMovies(query, 1)
            .scheduleIoToUi(threadScheduler)
            .subscribe({
                showMovies(it.movies)
            }, {
                eventsQueue.offer(SearchEvents.ErrorMessage(R.string.unknown_error_message))
            })
            .autoDispose()
    }

    private fun showDefaultImage() {
        state = state.copy(movies = null)
    }

    private fun showMovies(movies : List<Movie>) {
        state = state.copy(movies = movies)
    }

    private fun createInitialState(): SearchViewState =
        SearchViewState(movies = null)
}