package com.haraev.main.data

import com.haraev.core.data.LocalUserDataSource
import com.haraev.main.data.api.MainService
import com.haraev.main.data.model.request.DeleteSessionBody
import com.haraev.core.data.model.response.AccountDetailsResponse
import com.haraev.main.domain.repository.ProfileRepository
import io.reactivex.Completable
import io.reactivex.Single

class ProfileRepositoryImpl(
    private val mainService: MainService,
    private val localUserDataSource: LocalUserDataSource
) : ProfileRepository {

    override fun getAccountDetails(): Single<AccountDetailsResponse> {
        return mainService.getAccountDetails(localUserDataSource.requireSessionId())
    }

    override fun logout(): Completable {
        return mainService.deleteSession(DeleteSessionBody(localUserDataSource.requireSessionId()))
            .flatMapCompletable {
                localUserDataSource.sessionId = null
                localUserDataSource.userLogin = null
                localUserDataSource.userPassword = null
                localUserDataSource.userPinCodeHash = null
                localUserDataSource.biometricAct = null
                Completable.complete()
            }
    }
}