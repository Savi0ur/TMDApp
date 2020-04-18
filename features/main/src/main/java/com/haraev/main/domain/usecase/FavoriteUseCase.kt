package com.haraev.main.domain.usecase

import com.haraev.main.data.model.response.FavoriteMoviesResponse
import com.haraev.main.data.model.response.MovieDetailsResponse
import com.haraev.main.domain.repository.FavoriteRepository
import io.reactivex.Single
import javax.inject.Inject

class FavoriteUseCase @Inject constructor(
    private val favoriteRepository: FavoriteRepository
) {

    fun getFavoriteMovies() : Single<FavoriteMoviesResponse> =
        favoriteRepository.getFavoriteMovies()

    fun getMovieDetails(movieId: Int) : Single<MovieDetailsResponse> =
        favoriteRepository.getMovieDetails(movieId)

}