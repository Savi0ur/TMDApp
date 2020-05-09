package com.haraev.main.data

import com.haraev.core.data.LocalUserDataSource
import com.haraev.database.Database
import com.haraev.main.data.api.MainService
import com.haraev.main.data.model.request.MarkAsFavoriteBody
import com.haraev.main.data.model.response.FavoriteMoviesResponse
import com.haraev.main.data.model.response.MovieDetailsResponse
import com.haraev.main.data.model.response.convertToDomain
import com.haraev.main.domain.repository.FavoriteRepository
import io.reactivex.Completable
import io.reactivex.Single

class FavoriteRepositoryImpl(
    private val mainService: MainService,
    private val localUserDataSource: LocalUserDataSource,
    private val database: Database
) : FavoriteRepository {

    override fun getFavoriteMovies(): Single<FavoriteMoviesResponse> {
        return mainService.getFavoriteMovies(localUserDataSource.requireSessionId())
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

    override fun getMovieDetails(movieId: Int): Single<MovieDetailsResponse> {
        return fetchAndSaveMovieDetails(movieId)
            .andThen(getOfflineMovieDetails(movieId))
            .onErrorResumeNext(getOfflineMovieDetails(movieId))
    }

    override fun getOfflineFavoriteMovies(): Single<List<MovieDetailsResponse>> {
        return database
            .movieDao()
            .getAllFavorite()
            .map { list ->
                list.map { it.convertToDomain() }
            }
    }

    private fun fetchAndSaveMovieDetails(movieId: Int): Completable {
        return mainService
            .getMovieDetails(movieId)
            .map { it.convertToDB().copy(isFavorite = true) }
            .flatMapCompletable { database.movieDao().insertMovies(it) }
    }

    private fun getOfflineMovieDetails(serverId: Int): Single<MovieDetailsResponse> {
        return database
            .movieDao()
            .getByServerId(serverId)
            .map { it.convertToDomain() }
    }
}