package com.haraev.tmdapp.ui

import androidx.lifecycle.ViewModel
import com.haraev.core.aac.EventsQueue
import com.haraev.core.data.SessionLocalDataSource
import javax.inject.Inject

class MainViewModel @Inject constructor(
    private val sessionLocalDataSource: SessionLocalDataSource
) : ViewModel() {

    val eventsQueue = EventsQueue()

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