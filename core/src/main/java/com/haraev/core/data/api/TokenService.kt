package com.haraev.core.data.api

import com.haraev.core.data.model.request.SessionBody
import com.haraev.core.data.model.request.ValidateWithLoginBody
import com.haraev.core.data.model.response.SessionResponse
import com.haraev.core.data.model.response.TokenResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface TokenService {

    @GET(value = "authentication/token/new")
    fun getNewToken() : Call<TokenResponse>

    @POST(value = "authentication/token/validate_with_login")
    fun validateWithLogin(
        @Body validateWithLoginBody: ValidateWithLoginBody
    ) : Call<TokenResponse>

    @POST(value = "authentication/session/new")
    fun getNewSession(
        @Body sessionBody: SessionBody
    ) : Call<SessionResponse>

}