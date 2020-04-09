package com.haraev.authentication.presentation

import com.haraev.core.aac.Event

sealed class LoginEvents : Event {
    object NavigateToNextScreen : LoginEvents()
}