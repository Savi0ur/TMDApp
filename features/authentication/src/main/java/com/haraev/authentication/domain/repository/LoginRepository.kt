package com.haraev.authentication.domain.repository

interface LoginRepository {

    fun login(login: String, password: String)
}