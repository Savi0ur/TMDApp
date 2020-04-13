package com.haraev.authentication.presentation.makePin

import androidx.lifecycle.MutableLiveData
import com.haraev.core.aac.delegate
import com.haraev.core.ui.BaseViewModel
import javax.inject.Inject

class MakePinCodeViewModel @Inject constructor() : BaseViewModel() {

    private var pinCode = ""

    val uiState = MutableLiveData(createInitialState())
    private var state: MakePinCodeViewState by uiState.delegate()

    fun onKeyboardTextItemClicked(s: String) {
        pinCode += s
        updateUiPin()
        if (pinCode.length == 4) {
            navigateToNextScreen()
        }
    }

    fun onKeyboardBackspaceClicked() {
        if (pinCode.isNotEmpty()) {
            pinCode = pinCode.dropLast(1)
            updateUiPin()
        }
    }

    private fun navigateToNextScreen() {
        eventsQueue.offer(MakePinCodeEvents.NavigateToRepeatPinCodeScreen(pinCode))

        pinCode = ""
        updateUiPin()
    }

    private fun updateUiPin() {
        state = state.copy(pinCode = pinCode)
    }

    private fun createInitialState() = MakePinCodeViewState(
        pinCode = pinCode
    )
}