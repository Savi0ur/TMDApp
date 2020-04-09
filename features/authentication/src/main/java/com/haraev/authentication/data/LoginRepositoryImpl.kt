package com.haraev.authentication.data

import android.accounts.NetworkErrorException
import com.haraev.core.data.model.request.SessionBody
import com.haraev.core.data.model.request.ValidateWithLoginBody
import com.haraev.authentication.domain.repository.LoginRepository
import com.haraev.core.data.SessionLocalDataSource
import com.haraev.core.data.api.LoginService
import com.haraev.core.data.exception.ResponseException
import io.reactivex.Completable
import io.reactivex.Single

class LoginRepositoryImpl(
    private val loginService: LoginService,
    private val sessionLocalDataSource: SessionLocalDataSource
) : LoginRepository {

    override fun login(login: String, password: String): Completable {
        return loginService.getNewToken()
            .flatMap { response ->
                response.body()?.requestToken?.let {
                    loginService.validateWithLogin(
                        ValidateWithLoginBody(
                            login,
                            password,
                            it
                        )
                    )
                } ?: Single.error(ResponseException())
            }
            .flatMap { response ->
                response.body()?.requestToken?.let {
                    loginService.getNewSession(SessionBody(it))
                } ?: Single.error(ResponseException())
            }
            .flatMapCompletable { response ->
                sessionLocalDataSource.sessionId = response.body()?.sessionId
                sessionLocalDataSource.userLogin = login
                sessionLocalDataSource.userPassword = password
                Completable.complete()
            }
    }
}