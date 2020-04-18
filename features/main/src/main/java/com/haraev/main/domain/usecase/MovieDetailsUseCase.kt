package com.haraev.main.domain.usecase

import com.haraev.main.data.model.response.FavoriteMoviesResponse
import com.haraev.main.domain.repository.MovieDetailsRepository
import io.reactivex.Completable
import io.reactivex.Single
import javax.inject.Inject

class MovieDetailsUseCase @Inject constructor(
    private val movieDetailsRepository: MovieDetailsRepository
) {

    fun getFavoriteMovies(): Single<FavoriteMoviesResponse> {
        return movieDetailsRepository.getFavoriteMovies()
    }

    fun markAsFavorite(serverId: Int, isFavorite: Boolean): Completable {
        return movieDetailsRepository.markAsFavorite(serverId, isFavorite)
    }
}