package com.haraev.authentication.presentation.login

import com.haraev.core.ui.Event

sealed class LoginEvents : Event {
    object NavigateToNextScreen : LoginEvents()
}