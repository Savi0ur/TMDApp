package com.haraev.main.domain.usecase

import com.haraev.main.data.model.response.FavoriteMoviesResponse
import com.haraev.main.domain.repository.FavoriteRepository
import io.reactivex.Completable
import io.reactivex.Single
import javax.inject.Inject

class FavoriteUseCase @Inject constructor(
    private val favoriteRepository: FavoriteRepository
) {

    fun getFavoriteMovies(): Single<FavoriteMoviesResponse> =
        favoriteRepository.getFavoriteMovies()

    fun markAsFavorite(serverId: Int, isFavorite: Boolean): Completable =
        favoriteRepository.markAsFavorite(serverId, isFavorite)
}