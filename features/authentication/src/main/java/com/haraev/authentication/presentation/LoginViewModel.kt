package com.haraev.authentication.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.haraev.authentication.domain.usecase.LoginUseCase
import javax.inject.Inject

class LoginViewModel @Inject constructor(private val loginUseCase: LoginUseCase) : ViewModel() {

    private val _uiState = MutableLiveData<LoginViewState>()
    val uiState: LiveData<LoginViewState>
        get() = _uiState

    fun login(login: String, password: String) {
        loginUseCase.login(login, password)
    }

    fun loginDataChanged(login: String, password: String) {
        enableLogin(isLoginValid(login, password))
    }

    private fun isLoginValid(login: String, password: String): Boolean {
        return login.isNotBlank() && password.isNotBlank()
    }

    private fun enableLogin(enabled: Boolean) {
        emitUiState(enabled)
    }

    private fun emitUiState(enableLoginButton: Boolean = false) {
        val uiModel = LoginViewState(enableLoginButton)
        _uiState.value = uiModel
    }
}