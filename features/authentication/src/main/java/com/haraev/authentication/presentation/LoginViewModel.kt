package com.haraev.authentication.presentation

import androidx.lifecycle.ViewModel
import com.haraev.authentication.domain.usecase.LoginUseCase
import javax.inject.Inject

class LoginViewModel @Inject constructor(private val loginUseCase: LoginUseCase) : ViewModel() {

    fun login(login: String, password: String) {
        loginUseCase.login(login, password)
    }
}