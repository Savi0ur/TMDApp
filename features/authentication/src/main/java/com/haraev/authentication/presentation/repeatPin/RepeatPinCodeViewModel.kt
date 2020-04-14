package com.haraev.authentication.presentation.repeatPin

import androidx.lifecycle.MutableLiveData
import com.haraev.authentication.R
import com.haraev.authentication.domain.usecase.RepeatPinCodeUseCase
import com.haraev.core.aac.delegate
import com.haraev.core.common.ThreadScheduler
import com.haraev.core.common.scheduleIoToUi
import com.haraev.core.ui.BaseViewModel
import timber.log.Timber
import javax.inject.Inject

class RepeatPinCodeViewModel @Inject constructor(
    private val repeatPinCodeUseCase: RepeatPinCodeUseCase,
    private val scheduler: ThreadScheduler
) : BaseViewModel() {

    private var repeatPinCode = ""
    lateinit var pinCode: String

    val uiState = MutableLiveData(createInitialState())
    private var state: RepeatPinCodeViewState by uiState.delegate()

    fun onKeyboardTextItemClicked(s: String) {
        repeatPinCode += s
        updateUiPin()
        if (repeatPinCode.length == 4) {
            if (repeatPinCode == pinCode) {
                tryUseFingerPrint()
            } else {
                repeatPinCode = ""
                updateUiPin()
                showUiWrongPin()
            }
        }
    }

    fun onKeyboardBackspaceClicked() {
        if (repeatPinCode.isNotEmpty()) {
            repeatPinCode = repeatPinCode.dropLast(1)
            updateUiPin()
        }
    }

    fun setPin(pinCode: String) {
        this.pinCode = pinCode
    }

    fun biometricFailed() {
        biometricDone(false)
    }

    fun biometricCancelled() {
        biometricDone(false)
    }

    fun biometricSucceeded() {
        biometricDone(true)
    }

    fun biometricUnavailable() {
        biometricDone(false)
    }

    private fun biometricDone(isSuccess: Boolean) {
        repeatPinCodeUseCase.saveBiometricAct(isSuccess)
            .andThen(repeatPinCodeUseCase.savePinCode(pinCode))
            .scheduleIoToUi(scheduler)
            .subscribe({
                navigateToNextScreen()
            }, { e ->
                Timber.tag(TAG).e(e)
                showErrorMessage(R.string.pin_code_unknown_error_message)
            })
            .autoDispose()
    }

    private fun tryUseFingerPrint() {
        eventsQueue.offer(RepeatPinCodeEvents.TryUseFingerPrint)
    }

    private fun navigateToNextScreen() {
        eventsQueue.offer(RepeatPinCodeEvents.NavigateToMainFeature)
    }

    private fun createInitialState() = RepeatPinCodeViewState(
        repeatPinCode = repeatPinCode
    )

    private fun showUiWrongPin() {
        eventsQueue.offer(RepeatPinCodeEvents.WrongPin)
    }

    private fun updateUiPin() {
        state = state.copy(repeatPinCode = repeatPinCode)
    }

    companion object {
        private const val TAG = "RepeatPinCodeViewModel"
    }
}