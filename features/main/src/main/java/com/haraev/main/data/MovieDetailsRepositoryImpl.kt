package com.haraev.main.data

import com.haraev.core.data.LocalUserDataSource
import com.haraev.database.Database
import com.haraev.main.data.api.MainService
import com.haraev.main.data.model.Movie
import com.haraev.main.data.model.request.MarkAsFavoriteBody
import com.haraev.main.data.model.response.FavoriteMoviesResponse
import com.haraev.main.data.model.response.MovieDetailsResponse
import com.haraev.main.data.model.response.convertToDomain
import com.haraev.main.domain.repository.MovieDetailsRepository
import io.reactivex.Completable
import io.reactivex.Single

class MovieDetailsRepositoryImpl(
    private val mainService: MainService,
    private val localUserDataSource: LocalUserDataSource,
    private val database: Database
) : MovieDetailsRepository {

    override fun getFavoriteMovies(): Single<FavoriteMoviesResponse> {
        return mainService
            .getFavoriteMovies(localUserDataSource.requireSessionId())
            .onErrorResumeNext { getOfflineFavoriteMovies() }
    }

    override fun markAsFavorite(serverId: Int, isFavorite: Boolean): Completable {
        return mainService.markAsFavorite(
            sessionId = localUserDataSource.requireSessionId(),
            markAsFavoriteBody = MarkAsFavoriteBody(
                mediaId = serverId,
                favorite = isFavorite
            )
        )
    }

    private fun getOfflineFavoriteMovies(): Single<FavoriteMoviesResponse> {
        return getOfflineFavoriteMovieDetails()
            .map { list ->
                FavoriteMoviesResponse(
                    page = 1,
                    movies = list.map { it.convertToMovie() },
                    totalResults = list.size,
                    totalPages = 1
                )
            }
    }

    private fun getOfflineFavoriteMovieDetails(): Single<List<MovieDetailsResponse>> {
        return database.movieDao()
            .getAllFavorite()
            .map { list ->
                list.map { it.convertToDomain() }
            }
    }
}