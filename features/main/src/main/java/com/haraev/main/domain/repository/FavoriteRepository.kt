package com.haraev.main.domain.repository

import com.haraev.main.data.model.response.FavoriteMoviesResponse
import com.haraev.main.data.model.response.MovieDetailsResponse
import io.reactivex.Completable
import io.reactivex.Single

interface FavoriteRepository {

    fun getFavoriteMovies(): Single<FavoriteMoviesResponse>

    fun markAsFavorite(
        serverId: Int, isFavorite: Boolean
    ): Completable
}