package com.haraev.core.data.api

import com.haraev.core.data.LocalUserDataSource
import com.haraev.core.data.model.request.SessionBody
import com.haraev.core.data.model.request.ValidateWithLoginBody
import okhttp3.Authenticator
import okhttp3.Request
import okhttp3.Response
import okhttp3.Route

class SessionAuthenticator(
    private val localUserDataSource: LocalUserDataSource,
    private val loginService: LoginService

) : Authenticator {

    companion object {
        private const val SESSION_ID_QUERY_PARAMETER_KEY = "session_id"
    }

    @Synchronized
    override fun authenticate(route: Route?, response: Response): Request? {

        val currentSessionToken: String = localUserDataSource.sessionId ?: return null
        val requestSessionId: String? =
            response.request.url.queryParameter(SESSION_ID_QUERY_PARAMETER_KEY)

        return if (currentSessionToken == requestSessionId) {
            refreshToken()?.let { buildRequestWithNewAccessToken(response, it) }
        } else {
            buildRequestWithNewAccessToken(response, currentSessionToken)
        }
    }

    private fun buildRequestWithNewAccessToken(
        response: Response,
        newSessionId: String
    ): Request {
        return response.request
            .newBuilder()
            .url(
                response
                    .request
                    .url
                    .newBuilder()
                    .removeAllQueryParameters(SESSION_ID_QUERY_PARAMETER_KEY)
                    .addQueryParameter(SESSION_ID_QUERY_PARAMETER_KEY, newSessionId)
                    .build()
            )
            .build()
    }

    private fun refreshToken(): String? {
        val userLogin: String = localUserDataSource.userLogin ?: return null
        val userPassword: String = localUserDataSource.userPassword ?: return null

        val newSessionId = loginService.getNewToken()
            .flatMap { response ->
                loginService.validateWithLogin(
                    ValidateWithLoginBody(
                        userLogin,
                        userPassword,
                        response.requestToken
                    )
                )
            }
            .flatMap { response ->
                loginService.getNewSession(SessionBody(response.requestToken))
            }
            .blockingGet()
            .sessionId

        localUserDataSource.sessionId = newSessionId

        return newSessionId
    }
}