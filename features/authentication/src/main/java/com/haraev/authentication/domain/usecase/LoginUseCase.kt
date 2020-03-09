package com.haraev.authentication.domain.usecase

import com.haraev.authentication.domain.repository.LoginRepository
import javax.inject.Inject

class LoginUseCase @Inject constructor(private val loginRepository: LoginRepository) {

    fun login(login: String, password: String) {
        loginRepository.login(login, password)
    }
}