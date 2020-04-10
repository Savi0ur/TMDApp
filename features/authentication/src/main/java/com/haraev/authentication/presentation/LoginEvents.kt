package com.haraev.authentication.presentation

import com.haraev.core.ui.Event

sealed class LoginEvents : Event {
    object NavigateToNextScreen : LoginEvents()
}