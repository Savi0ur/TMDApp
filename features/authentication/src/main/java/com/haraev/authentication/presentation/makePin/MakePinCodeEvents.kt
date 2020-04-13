package com.haraev.authentication.presentation.makePin

import com.haraev.core.ui.Event

sealed class MakePinCodeEvents : Event {
    data class NavigateToRepeatPinCodeScreen(val pinCode: String) : MakePinCodeEvents()
}