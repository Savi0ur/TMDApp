package com.haraev.core.data.api

import com.haraev.core.data.SessionLocalDataSource
import com.haraev.core.data.model.request.SessionBody
import com.haraev.core.data.model.request.ValidateWithLoginBody
import okhttp3.Authenticator
import okhttp3.Request
import okhttp3.Response
import okhttp3.Route

class SessionAuthenticator(
    private val sessionLocalDataSource: SessionLocalDataSource,
    private val loginService: LoginService

) : Authenticator {

    companion object {
        private const val SESSION_ID_QUERY_PARAMETER_KEY = "session_id"
    }

    @Synchronized
    override fun authenticate(route: Route?, response: Response): Request? {

        val currentSessionToken: String = sessionLocalDataSource.sessionId ?: return null
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
        val userLogin: String = sessionLocalDataSource.userLogin ?: return null
        val userPassword: String = sessionLocalDataSource.userPassword ?: return null

        return loginService.getNewToken()
            .flatMap { response ->
                response.body()?.requestToken?.let {
                    loginService.validateWithLogin(
                        ValidateWithLoginBody(
                            userLogin,
                            userPassword,
                            it
                        )
                    )
                }
            }
            .flatMap { response ->
                response.body()?.requestToken?.let {
                    loginService.getNewSession(SessionBody(it))
                }
            }
            .blockingGet()
            .body()
            ?.sessionId
            ?.also { sessionId ->
                sessionLocalDataSource.sessionId = sessionId
                return sessionId
            }
    }
}