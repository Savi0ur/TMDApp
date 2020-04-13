package com.haraev.authentication.data.api

import com.haraev.main.data.model.request.DeleteSessionBody
import com.haraev.main.data.model.response.AccountDetailsResponse
import com.haraev.main.data.model.response.DeleteSessionResponse
import io.reactivex.Single
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.HTTP
import retrofit2.http.Query

interface AccountService {

    @GET(value = "account")
    fun getAccountDetails(
        @Query("session_id") sessionId: String
    ): Single<AccountDetailsResponse>

    @HTTP(method = "DELETE", path = "authentication/session", hasBody = true)
    fun deleteSession(
        @Body deleteSessionBody: DeleteSessionBody
    ) : Single<DeleteSessionResponse>
}