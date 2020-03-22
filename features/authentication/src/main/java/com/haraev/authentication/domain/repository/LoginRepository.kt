package com.haraev.authentication.domain.repository

import io.reactivex.Completable

interface LoginRepository {

    fun login(login: String, password: String) : Completable
}