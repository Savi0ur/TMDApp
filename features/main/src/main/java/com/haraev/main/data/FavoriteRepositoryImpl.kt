package com.haraev.main.data

import com.haraev.core.data.LocalUserDataSource
import com.haraev.main.data.api.MainService
import com.haraev.main.data.model.response.FavoriteMoviesResponse
import com.haraev.main.data.model.response.MovieDetailsResponse
import com.haraev.main.domain.repository.FavoriteRepository
import io.reactivex.Single

class FavoriteRepositoryImpl(
    private val mainService: MainService,
    private val localUserDataSource: LocalUserDataSource
) : FavoriteRepository {

    override fun getFavoriteMovies(): Single<FavoriteMoviesResponse> {
        return mainService.getFavoriteMovies(localUserDataSource.requireSessionId())
    }

    override fun getMovieDetails(movieId: Int): Single<MovieDetailsResponse> {
        return mainService.getMovieDetails(
            movieId = movieId
        )
    }
}