package com.haraev.tmdapp.ui

import com.haraev.core.data.LocalUserDataSource
import com.haraev.core.ui.BaseViewModel
import com.haraev.tmdapp.R
import com.scottyab.rootbeer.RootBeer
import javax.inject.Inject

class MainViewModel @Inject constructor(
    private val localUserDataSource: LocalUserDataSource,
    rootBeer: RootBeer
) : BaseViewModel() {

    init {
        if (rootBeer.isRooted) {
            showUiDialog(::navigateToNextScreen)
        } else {
            navigateToNextScreen()
        }
    }

    private fun navigateToNextScreen() {
        if (isSessionIdExist()) {
            navigateToUsePinCodeScreen()
        } else {
            navigateToLoginScreen()
        }
    }

    private fun isSessionIdExist(): Boolean =
        localUserDataSource.sessionId != null

    private fun showUiDialog(onDismissAction: () -> Unit) {
        eventsQueue.offer(MainEvents.ShowDialog(R.string.rooted_device_alert, onDismissAction))
    }

    private fun navigateToLoginScreen() {
        eventsQueue.offer(MainEvents.OpenLoginScreen)
    }

    private fun navigateToUsePinCodeScreen() {
        eventsQueue.offer(MainEvents.OpenUsePinCodeScreen)
    }

}