package com.haraev.main.data.api

import com.haraev.main.data.model.request.DeleteSessionBody
import com.haraev.main.data.model.response.AccountDetailsResponse
import com.haraev.main.data.model.response.DeleteSessionResponse
import com.haraev.main.data.model.response.SearchMoviesResponse
import io.reactivex.Single
import retrofit2.Response
import retrofit2.http.*

interface MainService {

    @GET(value = "account")
    fun getAccountDetails(
        @Query("session_id") sessionId: String
    ): Single<Response<AccountDetailsResponse>>

    @HTTP(method = "DELETE", path = "authentication/session", hasBody = true)
    fun deleteSession(
        @Body deleteSessionBody: DeleteSessionBody
    ) : Single<Response<DeleteSessionResponse>>


    @GET(value = "search/movie")
    fun getMovies(
        @Query("language") language: String,
        @Query("query") query: String,
        @Query("page") page: Int
    ): Single<Response<SearchMoviesResponse>>
}