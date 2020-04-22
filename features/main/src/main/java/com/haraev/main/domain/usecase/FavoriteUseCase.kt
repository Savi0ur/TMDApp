package com.haraev.main.domain.usecase

import com.haraev.main.data.model.response.MovieDetailsResponse
import com.haraev.main.domain.repository.FavoriteRepository
import io.reactivex.Single
import javax.inject.Inject

class FavoriteUseCase @Inject constructor(
    private val favoriteRepository: FavoriteRepository
) {

    fun getFavoriteMovies(): Single<List<MovieDetailsResponse>> =
        favoriteRepository.getFavoriteMovies()
            .flattenAsObservable { it.movies }
            .flatMap { movie ->
                favoriteRepository.getMovieDetails(movie.serverId).toObservable()
            }
            .collect(
                { ArrayList<MovieDetailsResponse>().toMutableList() },
                { list, item -> list.add(item) }
            )
            .map { it.toList() }
            .onErrorResumeNext {
                favoriteRepository
                    .getOfflineFavoriteMovies()
            }
}