package com.haraev.authentication.presentation

sealed class LoginViewCommand {
    object NavigateToNextScreen : LoginViewCommand()
}