package com.haraev.authentication.presentation.usePin

import com.haraev.core.ui.Event

sealed class UsePinCodeEvents : Event {
    object NavigateToMainFeature : UsePinCodeEvents()
    object NavigateToLoginScreen : UsePinCodeEvents()
    object TryUseFingerPrint: UsePinCodeEvents()
    data class WrongPin(val messageResId: Int) : UsePinCodeEvents()
}