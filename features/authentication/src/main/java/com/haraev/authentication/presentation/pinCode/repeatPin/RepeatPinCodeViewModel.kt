package com.haraev.authentication.presentation.pinCode.repeatPin

import androidx.lifecycle.MutableLiveData
import com.haraev.authentication.R
import com.haraev.authentication.domain.usecase.RepeatPinCodeUseCase
import com.haraev.authentication.presentation.pinCode.PinCodeViewModel
import com.haraev.core.aac.delegate
import com.haraev.core.common.ThreadScheduler
import com.haraev.core.common.scheduleIoToUi
import com.haraev.core.ui.BaseViewModel
import timber.log.Timber
import javax.inject.Inject

class RepeatPinCodeViewModel @Inject constructor(
    private val repeatPinCodeUseCase: RepeatPinCodeUseCase,
    private val scheduler: ThreadScheduler
) : BaseViewModel(),
    PinCodeViewModel {

    lateinit var pinCode: String

    val uiState = MutableLiveData(createInitialState())
    private var state: RepeatPinCodeViewState by uiState.delegate()

    override fun onKeyboardTextItemClicked(text: String) {
        updateUiPin(state.repeatPinCode + text)
        if (state.repeatPinCode.length == 4) {
            if (state.repeatPinCode == pinCode) {
                tryUseFingerPrint()
            } else {
                updateUiPin("")
                showUiWrongPin()
            }
        }
    }

    override fun onKeyboardBackspaceItemClicked() {
        if (state.repeatPinCode.isNotEmpty()) {
            updateUiPin(state.repeatPinCode.dropLast(1))
        }
    }

    fun setPin(pinCode: String) {
        this.pinCode = pinCode
    }

    override fun onBiometricFailed() {
        biometricDone(false)
    }

    override fun onBiometricCancelled() {
        biometricDone(false)
    }

    override fun onBiometricSucceed() {
        biometricDone(true)
    }

    override fun onBiometricUnavailable() {
        biometricDone(false)
    }

    private fun biometricDone(isSuccess: Boolean) {
        repeatPinCodeUseCase.saveBiometricAct(isSuccess)
            .andThen(repeatPinCodeUseCase.savePinCodeHash(pinCode.hashCode()))
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

    private fun createInitialState() = RepeatPinCodeViewState()

    private fun showUiWrongPin() {
        eventsQueue.offer(RepeatPinCodeEvents.WrongPin)
    }

    private fun updateUiPin(repeatPinCode: String) {
        state = state.copy(repeatPinCode = repeatPinCode)
    }

    companion object {
        private const val TAG = "RepeatPinCodeViewModel"
    }
}