package com.haraev.main.presentation.main

import com.haraev.core.common.ThreadScheduler
import com.haraev.core.common.scheduleIoToUi
import com.haraev.core.ui.BaseViewModel
import com.haraev.core.ui.ShowErrorMessage
import com.haraev.main.R
import com.haraev.main.presentation.BottomNavigationRouter
import com.haraev.main.presentation.BottomNavigationScreen
import timber.log.Timber
import javax.inject.Inject

class MainFeatureViewModel @Inject constructor(
    bottomNavigationRouter: BottomNavigationRouter,
    threadScheduler: ThreadScheduler
) : BaseViewModel() {

    init {
        bottomNavigationRouter.router
            .scheduleIoToUi(threadScheduler)
            .subscribe({ bottomNavigationScreen ->
                when (bottomNavigationScreen) {
                    BottomNavigationScreen.SCREEN_SEARCH -> navigateToSearchScreen()
                    BottomNavigationScreen.SCREEN_FAVORITE -> navigateToFavoriteScreen()
                    BottomNavigationScreen.SCREEN_PROFILE -> navigateToProfileScreen()
                    else -> showError()
                }
            }, { e ->
                Timber.tag(TAG).e(e)
                showError()
            })
            .autoDispose()
    }

    private fun navigateToSearchScreen() {
        eventsQueue.offer(MainFeatureEvents.NavigateToSearchScreen)
    }

    private fun navigateToFavoriteScreen() {
        eventsQueue.offer(MainFeatureEvents.NavigateToFavoriteScreen)
    }

    private fun navigateToProfileScreen() {
        eventsQueue.offer(MainFeatureEvents.NavigateToProfileScreen)
    }

    private fun showError() {
        eventsQueue.offer(ShowErrorMessage(R.string.unknown_error_message))
    }

    companion object {
        private const val TAG = "MainFeatureViewModel"
    }
}