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
    private val tokenService: TokenService

) : Authenticator {

    companion object {
        private const val SESSION_ID_QUERY_PARAMETER_KEY = "session_id"
    }

    @Synchronized
    override fun authenticate(route: Route?, response: Response): Request? {

        val currentSessionToken: String = sessionLocalDataSource.sessionId ?: return null
        val requestSessionId: String? =
            response.request.url.queryParameter(SESSION_ID_QUERY_PARAMETER_KEY)

        if (currentSessionToken == requestSessionId) {
            val newSessionId = refreshToken() ?: return null
            return buildRequestWithNewAccessToken(response, newSessionId)
        } else {
            buildRequestWithNewAccessToken(response, currentSessionToken)
        }

        return null
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

        getNewToken()?.let { requestToken ->
            validateLogin(userLogin, userPassword, requestToken)?.let {
                return getNewSessionId(requestToken)
            }
        }

        return null
    }

    private fun getNewToken(): String? {
        tokenService.getNewToken()
            .execute()
            .body()
            ?.requestToken
            ?.let { requestToken ->
                return requestToken
            }
        return null
    }

    private fun validateLogin(
        userLogin: String,
        userPassword: String,
        requestToken: String
    ): String? {
        tokenService.validateWithLogin(
            ValidateWithLoginBody(
                userLogin,
                userPassword,
                requestToken
            )
        ).execute()
            .body()
            ?.requestToken
            ?.let {
                return it
            }
        return null
    }

    private fun getNewSessionId(requestToken: String): String? {
        tokenService.getNewSession(SessionBody(requestToken))
            .execute().body()
            ?.sessionId
            ?.let { sessionId ->
                sessionLocalDataSource.sessionId = sessionId
                return sessionId
            }
        return null
    }
}