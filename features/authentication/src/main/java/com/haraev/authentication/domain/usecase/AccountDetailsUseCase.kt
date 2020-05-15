package com.haraev.authentication.domain.usecase

import com.haraev.authentication.domain.repository.AccountRepository
import com.haraev.core.data.model.response.AccountDetailsResponse
import io.reactivex.Completable
import io.reactivex.Single
import javax.inject.Inject

class AccountDetailsUseCase @Inject constructor(
    private val accountRepository: AccountRepository
) {

    fun logout() : Completable =
        accountRepository.logout()

    fun getAccountDetails() : Single<AccountDetailsResponse> =
        accountRepository.getAccountDetails()
}