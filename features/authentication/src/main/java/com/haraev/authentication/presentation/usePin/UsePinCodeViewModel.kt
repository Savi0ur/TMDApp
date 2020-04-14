package com.haraev.authentication.presentation.usePin

import androidx.lifecycle.MutableLiveData
import com.haraev.authentication.R
import com.haraev.authentication.domain.usecase.UsePinCodeUseCase
import com.haraev.authentication.presentation.repeatPin.RepeatPinCodeViewModel
import com.haraev.core.aac.delegate
import com.haraev.core.common.ThreadScheduler
import com.haraev.core.common.scheduleIoToUi
import com.haraev.core.ui.BaseViewModel
import timber.log.Timber
import javax.inject.Inject

class UsePinCodeViewModel @Inject constructor(
    private val usePinCodeUseCase: UsePinCodeUseCase,
    private val scheduler: ThreadScheduler
) : BaseViewModel() {

    private var pinCode = ""

    var biometricAct = false

    val uiState = MutableLiveData(createInitialState())
    private var state: UsePinCodeViewState by uiState.delegate()

    init {
        showProgressBar(true)
        usePinCodeUseCase.getBiometricAct()
            .scheduleIoToUi(scheduler)
            .subscribe({ biometricAct ->
                this.biometricAct = biometricAct
                showProgressBar(false)
                if (biometricAct) {
                    tryUseFingerPrint()
                }
            }, { e ->
                showProgressBar(false)
                Timber.tag(TAG).e(e)
                showErrorMessage(R.string.unknown_error_message)
            })
            .autoDispose()
    }

    fun onKeyboardTextItemClicked(s: String) {
        pinCode += s
        updateUiPin()
        if (pinCode.length == 4) {
            if (biometricAct) {
                checkPinCode()
            } else {

            }
        }
    }

    fun onKeyboardBackspaceClicked() {
        if (pinCode.isNotEmpty()) {
            pinCode = pinCode.dropLast(1)
            updateUiPin()
        }
    }

    fun logout() {
        showProgressBar(true)
        usePinCodeUseCase.logout()
            .scheduleIoToUi(scheduler)
            .subscribe({
                showProgressBar(false)
                navigateToLoginScreen()
            }, { e ->
                showProgressBar(false)
                Timber.tag(TAG).e(e)
                showErrorMessage(R.string.unknown_error_message)
            })
            .autoDispose()
    }

    private fun tryUseFingerPrint() {
        eventsQueue.offer(UsePinCodeEvents.TryUseFingerPrint)
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
        if (isSuccess) {
            navigateToNextScreen()
        }
    }

    private fun checkPinCode() {
        usePinCodeUseCase.getPinCode()
            .scheduleIoToUi(scheduler)
            .subscribe({ savedPinCode ->
                if (pinCode == savedPinCode) {
                    navigateToNextScreen()
                } else {
                    pinCode = ""
                    updateUiPin()
                    showUiWrongPin()
                }
            }, { e ->
                Timber.tag(TAG).e(e)
                pinCode = ""
                updateUiPin()
                showErrorMessage(R.string.pin_code_unknown_error_message)
            })
            .autoDispose()
    }

    private fun navigateToLoginScreen() {
        eventsQueue.offer(UsePinCodeEvents.NavigateToLoginScreen)
    }

    private fun navigateToNextScreen() {
        eventsQueue.offer(UsePinCodeEvents.NavigateToMainFeature)
    }

    private fun showProgressBar(visible: Boolean) {
        state = state.copy(showProgressBar = visible)
    }

    private fun showUiWrongPin() {
        eventsQueue.offer(UsePinCodeEvents.WrongPin(R.string.wrong_pin_code))
    }

    private fun updateUiPin() {
        state = state.copy(pinCode = pinCode)
    }

    private fun createInitialState() = UsePinCodeViewState(
        pinCode = pinCode
    )

    companion object {
        private const val TAG = "UsePinCodeViewModel"
    }
}