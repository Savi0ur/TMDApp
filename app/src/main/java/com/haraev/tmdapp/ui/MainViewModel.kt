package com.haraev.tmdapp.ui

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.haraev.core.data.SessionLocalDataSource
import javax.inject.Inject

class MainViewModel @Inject constructor(
    private val sessionLocalDataSource: SessionLocalDataSource
) : ViewModel() {

    val uiCommand = MutableLiveData<MainViewCommand>()

    fun getNavigationState() {
        if (isSessionIdExist()) {
            navigateToSearchScreen()
        } else {
            navigateToLoginScreen()
        }
    }

    private fun isSessionIdExist(): Boolean =
        sessionLocalDataSource.sessionId != null

    private fun navigateToLoginScreen() {
        emitUiCommand(MainViewCommand.OpenLoginScreen)
    }

    private fun navigateToSearchScreen() {
        emitUiCommand(MainViewCommand.OpenSearchScreen)
    }

    private fun emitUiCommand(mainViewCommand: MainViewCommand) {
        uiCommand.value = mainViewCommand
    }

}