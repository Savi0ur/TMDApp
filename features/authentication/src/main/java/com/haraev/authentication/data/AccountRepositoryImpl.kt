package com.haraev.authentication.data

import com.haraev.authentication.data.api.AccountService
import com.haraev.authentication.domain.repository.AccountRepository
import com.haraev.core.data.LocalUserDataSource
import com.haraev.main.data.model.request.DeleteSessionBody
import com.haraev.core.data.model.response.AccountDetailsResponse
import io.reactivex.Completable
import io.reactivex.Single

class AccountRepositoryImpl(
    private val accountService: AccountService,
    private val localUserDataSource: LocalUserDataSource
) : AccountRepository {

    override fun getAccountDetails(): Single<AccountDetailsResponse> {
        return accountService.getAccountDetails(localUserDataSource.requireSessionId())
    }

    override fun logout(): Completable {
        return accountService.deleteSession(DeleteSessionBody(localUserDataSource.requireSessionId()))
            .flatMapCompletable {
                localUserDataSource.sessionId = null
                localUserDataSource.userLogin = null
                localUserDataSource.userPassword = null
                localUserDataSource.userPinCodeHash = null
                Completable.complete()
            }
    }
}