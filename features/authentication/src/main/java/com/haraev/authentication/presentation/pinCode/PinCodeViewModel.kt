package com.haraev.authentication.presentation.pinCode

interface PinCodeViewModel {

    fun onKeyboardTextItemClicked(text: String)

    fun onKeyboardBackspaceItemClicked()

    fun onKeyboardExitItemClicked() {}

    fun onBiometricCancelled() {}

    fun onBiometricFailed() {}

    fun onBiometricSucceed() {}

    fun onBiometricUnavailable() {}
}