package com.haraev.authentication.presentation.pinCode.repeatPin

import com.haraev.core.ui.Event

sealed class RepeatPinCodeEvents : Event {
    object NavigateToMainFeature : RepeatPinCodeEvents()
    object WrongPin: RepeatPinCodeEvents()
    object TryUseFingerPrint: RepeatPinCodeEvents()
}