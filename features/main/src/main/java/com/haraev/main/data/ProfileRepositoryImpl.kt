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
            .flatMap {
                it.body()?.let { accountDetailResponse ->
                    Single.just(accountDetailResponse)
                } ?: Single.error(IllegalStateException("Пустое тело ответа"))
            }
    }

    override fun logout(): Completable {
        return mainService.deleteSession(DeleteSessionBody(sessionLocalDataSource.requireSessionId()))
            .flatMapCompletable {
                it.body()?.let {
                    sessionLocalDataSource.sessionId = null
                    sessionLocalDataSource.userLogin = null
                    sessionLocalDataSource.userPassword = null
                    Completable.complete()
                } ?: Completable.error(IllegalStateException("Пустое тело ответа"))
            }
    }
}