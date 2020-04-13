package com.haraev.authentication.domain.repository

import com.haraev.main.data.model.response.AccountDetailsResponse
import io.reactivex.Completable
import io.reactivex.Single

interface AccountRepository {

    fun getAccountDetails() : Single<AccountDetailsResponse>

    fun logout() : Completable
}