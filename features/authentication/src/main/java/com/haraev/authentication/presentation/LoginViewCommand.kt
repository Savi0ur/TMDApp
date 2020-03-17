package com.haraev.authentication.presentation

sealed class LoginViewCommand {
    data class ChangeProgressBarVisibility(val visible: Boolean) : LoginViewCommand()

    data class ChangeEnterButtonEnable(val enabled: Boolean) : LoginViewCommand()

    data class ChangeLoginPasswordFieldsEnable(val enabled: Boolean) : LoginViewCommand()

    data class ShowErrorMessage(val resId: Int) : LoginViewCommand()
}