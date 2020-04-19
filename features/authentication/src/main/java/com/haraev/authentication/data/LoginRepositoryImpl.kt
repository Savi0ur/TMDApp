package com.haraev.authentication.data

import com.haraev.core.data.model.request.SessionBody
import com.haraev.core.data.model.request.ValidateWithLoginBody
import com.haraev.authentication.domain.repository.LoginRepository
import com.haraev.core.data.SessionLocalDataSource
import com.haraev.core.data.api.LoginService
import io.reactivex.Completable

class LoginRepositoryImpl(
    private val loginService: LoginService,
    private val sessionLocalDataSource: SessionLocalDataSource
) : LoginRepository {

    override fun login(login: String, password: String): Completable {
        return loginService.getNewToken()
            .flatMap { response ->
                loginService.validateWithLogin(
                    ValidateWithLoginBody(
                        login,
                        password,
                        response.requestToken
                    )
                )
            }
            .flatMap { response ->
                loginService.getNewSession(SessionBody(response.requestToken))
            }
            .flatMapCompletable { response ->
                sessionLocalDataSource.sessionId = response.sessionId
                sessionLocalDataSource.userLogin = login
                sessionLocalDataSource.userPassword = password
                Completable.complete()
            }
    }
}