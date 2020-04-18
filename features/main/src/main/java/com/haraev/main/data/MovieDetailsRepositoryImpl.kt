package com.haraev.main.data

import com.haraev.core.data.LocalUserDataSource
import com.haraev.main.data.api.MainService
import com.haraev.main.data.model.request.MarkAsFavoriteBody
import com.haraev.main.data.model.response.FavoriteMoviesResponse
import com.haraev.main.domain.repository.MovieDetailsRepository
import io.reactivex.Completable
import io.reactivex.Single

class MovieDetailsRepositoryImpl(
    private val mainService: MainService,
    private val localUserDataSource: LocalUserDataSource
) : MovieDetailsRepository {

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
}