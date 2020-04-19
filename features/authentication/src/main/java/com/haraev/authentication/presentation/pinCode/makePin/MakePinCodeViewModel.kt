package com.haraev.authentication.presentation.pinCode.makePin

import androidx.lifecycle.MutableLiveData
import com.haraev.authentication.presentation.pinCode.PinCodeViewModel
import com.haraev.core.aac.delegate
import com.haraev.core.ui.BaseViewModel
import javax.inject.Inject

class MakePinCodeViewModel @Inject constructor() : BaseViewModel(),
    PinCodeViewModel {

    val uiState = MutableLiveData(createInitialState())
    private var state: MakePinCodeViewState by uiState.delegate()

    override fun onKeyboardTextItemClicked(text: String) {
        updateUiPin(state.pinCode + text)
        if (state.pinCode.length == 4) {
            navigateToNextScreen()
        }
    }

    override fun onKeyboardBackspaceItemClicked() {
        if (state.pinCode.isNotEmpty()) {
            updateUiPin(state.pinCode.dropLast(1))
        }
    }

    private fun navigateToNextScreen() {
        eventsQueue.offer(MakePinCodeEvents.NavigateToRepeatPinCodeScreen(state.pinCode))

        updateUiPin("")
    }

    private fun updateUiPin(pinCode: String) {
        state = state.copy(pinCode = pinCode)
    }

    private fun createInitialState() = MakePinCodeViewState()
}