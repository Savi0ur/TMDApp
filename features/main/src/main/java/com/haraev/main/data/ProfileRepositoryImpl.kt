package com.haraev.main.data

import com.haraev.core.data.SessionLocalDataSource
import com.haraev.main.data.api.MainService
import com.haraev.main.data.model.request.DeleteSessionBody
import com.haraev.main.data.model.response.AccountDetailsResponse
import com.haraev.main.domain.repository.ProfileRepository
import io.reactivex.Completable
import io.reactivex.Single

class ProfileRepositoryImpl(
    private val mainService: MainService,
    private val sessionLocalDataSource: SessionLocalDataSource
) : ProfileRepository {

    override fun getAccountDetails(): Single<AccountDetailsResponse> {
        return mainService.getAccountDetails(sessionLocalDataSource.requireSessionId())
            .flatMap { response ->
                Single.just(response.body())
            }
    }

    override fun logout(): Completable {
        return mainService.deleteSession(DeleteSessionBody(sessionLocalDataSource.requireSessionId()))
            .flatMapCompletable {
                sessionLocalDataSource.sessionId = null
                sessionLocalDataSource.userLogin = null
                sessionLocalDataSource.userPassword = null
                Completable.complete()
            }
    }
}