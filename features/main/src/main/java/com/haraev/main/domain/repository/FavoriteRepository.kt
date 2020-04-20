package com.haraev.main.domain.repository

import com.haraev.main.data.model.response.FavoriteMoviesResponse
import com.haraev.main.data.model.response.MovieDetailsResponse
import io.reactivex.Single

interface FavoriteRepository {

    fun getFavoriteMovies(): Single<FavoriteMoviesResponse>

    fun getMovieDetails(
        movieId: Int
    ): Single<MovieDetailsResponse>

    fun getOfflineFavoriteMovies(): Single<List<MovieDetailsResponse>>
}