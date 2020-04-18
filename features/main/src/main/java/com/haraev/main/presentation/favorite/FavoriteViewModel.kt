package com.haraev.main.presentation.favorite

import androidx.lifecycle.MutableLiveData
import com.haraev.core.aac.delegate
import com.haraev.core.common.ThreadScheduler
import com.haraev.core.common.scheduleIoToUi
import com.haraev.core.ui.BaseViewModel
import com.haraev.main.R
import com.haraev.main.data.model.response.MovieDetailsResponse
import com.haraev.main.domain.usecase.FavoriteUseCase
import com.haraev.main.presentation.BottomNavigationRouter
import com.haraev.main.presentation.search.SearchViewModel
import io.reactivex.Observable
import timber.log.Timber
import javax.inject.Inject

class FavoriteViewModel @Inject constructor(
    private val favoriteUseCase: FavoriteUseCase,
    private val threadScheduler: ThreadScheduler,
    private val bottomNavigationRouter: BottomNavigationRouter
) : BaseViewModel() {

    val uiState = MutableLiveData<FavoriteViewState>(createInitialState())
    private var state: FavoriteViewState by uiState.delegate()

    init {
        loadFavoriteMovies()
    }

    fun onSearchEditTextTextChanged(observable: Observable<String>) {
        observable
            .distinctUntilChanged()
            .scheduleIoToUi(threadScheduler)
            .subscribe({ query ->
                changeUiFilterQuery(query)
            }, { e ->
                Timber.tag(TAG).e(e)
                showErrorMessage(R.string.unknown_error_message)
            })
            .autoDispose()
    }

    fun onSearchNewMoviesTextClicked() {
        navigateToSearchScreen()
    }

    fun onCloseImageClicked() {
        state = state.copy(searchViewVisibility = false)
    }

    fun onSearchIconClicked() {
        state = state.copy(searchViewVisibility = true)
    }

    fun loadFavoriteMovies() {
        changeProgressBarState(true)
        favoriteUseCase.getFavoriteMovies()
            .flattenAsObservable { it.movies }
            .flatMap { movie ->
                favoriteUseCase.getMovieDetails(movie.serverId).toObservable()
            }
            .collect(
                { ArrayList<MovieDetailsResponse>() },
                { list, item -> list.add(item) }
            )
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
    }

    private fun changeUiFilterQuery(query: String) {
        state = state.copy(searchQuery = query)
    }

    private fun showMovies(movies: List<MovieDetailsResponse>) {
        if (movies != state.movies) {
            state = state.copy(movies = movies)
        }
    }

    private fun changeProgressBarState(visible: Boolean) {
        state = state.copy(progressBarVisibility = visible)
    }

    private fun navigateToSearchScreen() {
        bottomNavigationRouter.navigateToSearchScreen()
    }

    private fun createInitialState(): FavoriteViewState =
        FavoriteViewState()

    companion object {
        private const val TAG = "FavoriteViewModel"
    }
}