package com.haraev.main.presentation.moviedetails

import androidx.lifecycle.MutableLiveData
import com.haraev.core.aac.delegate
import com.haraev.core.common.ThreadScheduler
import com.haraev.core.common.scheduleIoToUi
import com.haraev.core.ui.BaseViewModel
import com.haraev.main.R
import com.haraev.main.domain.usecase.FavoriteUseCase
import timber.log.Timber
import javax.inject.Inject

class MovieDetailsViewModel @Inject constructor(
    private val favoriteUseCase: FavoriteUseCase,
    private val threadScheduler: ThreadScheduler
) : BaseViewModel() {

    private var movieServerId: Int? = null

    val uiState = MutableLiveData(createInitialState())
    private var state by uiState.delegate()

    fun initMovieId(movieId: Int) {
        movieServerId = movieId
        checkMovie(movieId)
    }

    fun markAsFavorite(isFavorite: Boolean) {
        movieServerId?.let { id ->
            showUiMarkIsFavoriteInProcess(true)
            favoriteUseCase
                .markAsFavorite(
                    serverId = id,
                    isFavorite = isFavorite
                )
                .scheduleIoToUi(threadScheduler)
                .subscribe({
                    showUiFavoriteMovie(isFavorite)
                    showUiMarkIsFavoriteInProcess(false)
                }, { e ->
                    showUiFavoriteMovie(!isFavorite)
                    showUiMarkIsFavoriteInProcess(false)
                    Timber.tag(TAG).e(e)
                    showErrorMessage(R.string.unknown_error_message)
                })
        } ?: showErrorMessage(R.string.unknown_error_message)
    }

    private fun checkMovie(serverId: Int) {
        changeProgressBarState(true)
        favoriteUseCase.getFavoriteMovies()
            .scheduleIoToUi(threadScheduler)
            .map { it.movies }
            .map { movies ->
                movies.map { it.serverId }
            }
            .subscribe({ favoriteIdList ->
                changeProgressBarState(false)
                showUiFavoriteMovie(favoriteIdList.contains(serverId))
            }, { e ->
                changeProgressBarState(false)
                Timber.tag(TAG).e(e)
                showErrorMessage(R.string.unknown_error_message)
            })
            .autoDispose()
    }

    private fun showUiMarkIsFavoriteInProcess(inProcess: Boolean) {
        state = state.copy(markAsFavoriteInProcess = inProcess)
    }

    private fun changeProgressBarState(visible: Boolean) {
        state = state.copy(progressBarVisibility = visible)
    }

    private fun showUiFavoriteMovie(isFavorite: Boolean) {
        state = state.copy(isFavoriteMovie = isFavorite)
    }

    private fun createInitialState(): MovieDetailsViewState = MovieDetailsViewState()

    companion object {
        private const val TAG = "MovieDetailsViewModel"
    }
}