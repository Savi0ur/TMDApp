package com.haraev.main.domain.repository

import com.haraev.core.data.model.response.AccountDetailsResponse
import io.reactivex.Completable
import io.reactivex.Single

interface ProfileRepository {

    fun getAccountDetails() : Single<AccountDetailsResponse>

    fun logout() : Completable
}