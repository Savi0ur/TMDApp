package com.haraev.authentication.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.haraev.authentication.domain.usecase.LoginUseCase
import javax.inject.Inject

class LoginViewModelFactory @Inject constructor(
    private val loginUseCase: LoginUseCase
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass != LoginViewModel::class.java) {
            throw IllegalArgumentException("Unknown ViewModel class")
        }
        return LoginViewModel(loginUseCase) as T
    }
}