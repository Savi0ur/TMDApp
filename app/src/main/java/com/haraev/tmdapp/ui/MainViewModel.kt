package com.haraev.tmdapp.ui

import com.haraev.core.data.LocalUserDataSource
import com.haraev.core.ui.BaseViewModel
import com.scottyab.rootbeer.RootBeer
import javax.inject.Inject

class MainViewModel @Inject constructor(
    private val localUserDataSource: LocalUserDataSource,
    rootBeer: RootBeer
) : BaseViewModel() {

    init {
        if (rootBeer.isRooted) {
            throw IllegalStateException("rooted device")
        } else {
            if (isSessionIdExist()) {
                navigateToUsePinCodeScreen()
            } else {
                navigateToLoginScreen()
            }
        }
    }

    private fun isSessionIdExist(): Boolean =
        localUserDataSource.sessionId != null

    private fun navigateToLoginScreen() {
        eventsQueue.offer(MainEvents.OpenLoginScreen)
    }

    private fun navigateToUsePinCodeScreen() {
        eventsQueue.offer(MainEvents.OpenUsePinCodeScreen)
    }

}