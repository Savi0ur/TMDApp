package com.haraev.tmdapp.ui

import com.haraev.core.data.SessionLocalDataSource
import com.haraev.core.ui.BaseViewModel
import javax.inject.Inject

class MainViewModel @Inject constructor(
    private val sessionLocalDataSource: SessionLocalDataSource
) : BaseViewModel() {

    init {
        if (isSessionIdExist()) {
            navigateToSearchScreen()
        } else {
            navigateToLoginScreen()
        }
    }

    private fun isSessionIdExist(): Boolean =
        sessionLocalDataSource.sessionId != null

    private fun navigateToLoginScreen() {
        eventsQueue.offer(MainEvents.OpenLoginScreen)
    }

    private fun navigateToSearchScreen() {
        eventsQueue.offer(MainEvents.OpenSearchScreen)
    }

}