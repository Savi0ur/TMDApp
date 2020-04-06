package com.haraev.authentication.presentation

data class LoginViewState(
    val progressBarVisibility : Boolean = false,
    val enterButtonEnable : Boolean = false,
    val loginAndPasswordFieldsEnable : Boolean = true,
    val errorMessage : Int? = null
)