package com.haraev.authentication.presentation.login

data class LoginViewState(
    val progressBarVisibility : Boolean = false,
    val enterButtonEnable : Boolean = false,
    val loginAndPasswordFieldsEnable : Boolean = true,
    val errorMessage : Int? = null
)