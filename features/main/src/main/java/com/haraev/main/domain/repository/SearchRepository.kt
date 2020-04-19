package com.haraev.main.domain.repository

import com.haraev.main.data.model.response.MovieDetailsResponse
import com.haraev.main.data.model.response.SearchMoviesResponse
import io.reactivex.Single

interface SearchRepository {

    fun getMovies(
        query: String,
        page: Int
    ): Single<SearchMoviesResponse>

    fun getMovieDetails(
        movieId: Int
    ): Single<MovieDetailsResponse>
}