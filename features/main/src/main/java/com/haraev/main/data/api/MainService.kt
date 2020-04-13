package com.haraev.main.data.api

import com.haraev.main.data.common.ApiLanguageEnum
import com.haraev.main.data.model.request.DeleteSessionBody
import com.haraev.main.data.model.response.AccountDetailsResponse
import com.haraev.main.data.model.response.DeleteSessionResponse
import com.haraev.main.data.model.response.MovieDetailsResponse
import com.haraev.main.data.model.response.SearchMoviesResponse
import io.reactivex.Single
import retrofit2.Response
import retrofit2.http.*

interface MainService {

    @GET(value = "account")
    fun getAccountDetails(
        @Query("session_id") sessionId: String
    ): Single<AccountDetailsResponse>

    @HTTP(method = "DELETE", path = "authentication/session", hasBody = true)
    fun deleteSession(
        @Body deleteSessionBody: DeleteSessionBody
    ) : Single<DeleteSessionResponse>

    @GET(value = "search/movie")
    fun getMovies(
        @Query("language") language: String = ApiLanguageEnum.RU.value,
        @Query("query") query: String,
        @Query("page") page: Int
    ): Single<SearchMoviesResponse>

    @GET(value = "movie/{movie_id}")
    fun getMovieDetails(
        @Path("movie_id") movieId: Int,
        @Query("language") language: String  = ApiLanguageEnum.RU.value
    ): Single<MovieDetailsResponse>
}