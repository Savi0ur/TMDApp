package com.haraev.authentication.presentation.pinCode.usePin

import androidx.lifecycle.MutableLiveData
import com.haraev.authentication.R
import com.haraev.authentication.domain.usecase.AccountDetailsUseCase
import com.haraev.authentication.domain.usecase.UsePinCodeUseCase
import com.haraev.authentication.presentation.pinCode.PinCodeViewModel
import com.haraev.core.aac.delegate
import com.haraev.core.common.ThreadScheduler
import com.haraev.core.common.scheduleIoToUi
import com.haraev.core.ui.BaseViewModel
import timber.log.Timber
import javax.inject.Inject

class UsePinCodeViewModel @Inject constructor(
    private val usePinCodeUseCase: UsePinCodeUseCase,
    private val accountDetailsUseCase: AccountDetailsUseCase,
    private val scheduler: ThreadScheduler
) : BaseViewModel(),
    PinCodeViewModel {

    private var biometricAct = false

    val uiState = MutableLiveData(createInitialState())
    private var state: UsePinCodeViewState by uiState.delegate()

    init {
        checkBiometricAct()
        loadAccountData()
    }

    override fun onKeyboardTextItemClicked(text: String) {
        updateUiPin(state.pinCode + text)
        if (state.pinCode.length == 4) {
            checkPinCode()
        }
    }

    override fun onKeyboardBackspaceItemClicked() {
        if (state.pinCode.isNotEmpty()) {
            updateUiPin(state.pinCode.dropLast(1))
        }
    }

    override fun onKeyboardExitItemClicked() {
        showProgressBar(true)
        accountDetailsUseCase.logout()
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

    private fun loadAccountData() {
        showProgressBar(true)
        accountDetailsUseCase.getAccountDetails()
            .scheduleIoToUi(scheduler)
            .subscribe({ accountDetailsResponse ->
                showProgressBar(false)
                if (accountDetailsResponse.name.isNotEmpty()) {
                    showUserName(accountDetailsResponse.name)
                } else {
                    showUserName(accountDetailsResponse.username)
                }
            }, { e ->
                showProgressBar(false)
                Timber.tag(TAG).e(e)
                showErrorMessage(R.string.unknown_error_message)
            })
            .autoDispose()
    }

    private fun checkBiometricAct() {
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

    private fun tryUseFingerPrint() {
        eventsQueue.offer(UsePinCodeEvents.TryUseFingerPrint)
    }

    private fun biometricDone(isSuccess: Boolean) {
        if (isSuccess) {
            navigateToNextScreen()
        }
    }

    private fun checkPinCode() {
        usePinCodeUseCase.getPinCode()
            .scheduleIoToUi(scheduler)
            .subscribe({ savedPinCodeHash ->
                if (state.pinCode.hashCode() == savedPinCodeHash) {
                    navigateToNextScreen()
                } else {
                    updateUiPin("")
                    showUiWrongPin()
                }
            }, { e ->
                Timber.tag(TAG).e(e)
                updateUiPin("")
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

    private fun showUserName(userName: String) {
        state = state.copy(userName = userName)
    }

    private fun showProgressBar(visible: Boolean) {
        state = state.copy(showProgressBar = visible)
    }

    private fun showUiWrongPin() {
        eventsQueue.offer(UsePinCodeEvents.WrongPin(R.string.wrong_pin_code))
    }

    private fun updateUiPin(pinCode: String) {
        state = state.copy(pinCode = pinCode)
    }

    private fun createInitialState() = UsePinCodeViewState()

    companion object {
        private const val TAG = "UsePinCodeViewModel"
    }
}