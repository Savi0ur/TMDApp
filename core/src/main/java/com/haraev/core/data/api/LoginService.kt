package com.haraev.core.data.api

import com.haraev.core.data.model.request.SessionBody
import com.haraev.core.data.model.request.ValidateWithLoginBody
import com.haraev.core.data.model.response.SessionResponse
import com.haraev.core.data.model.response.TokenResponse
import io.reactivex.Single
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface LoginService {

    @GET(value = "authentication/token/new")
    fun getNewToken() : Single<TokenResponse>

    @POST(value = "authentication/token/validate_with_login")
    fun validateWithLogin(
        @Body validateWithLoginBody: ValidateWithLoginBody
    ) : Single<TokenResponse>

    @POST(value = "authentication/session/new")
    fun getNewSession(
        @Body sessionBody: SessionBody
    ) : Single<SessionResponse>
}