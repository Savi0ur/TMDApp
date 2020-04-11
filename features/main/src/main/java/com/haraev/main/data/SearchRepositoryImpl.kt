package com.haraev.main.data

import com.haraev.main.data.api.MainService
import com.haraev.main.data.model.response.MovieDetailsResponse
import com.haraev.main.data.model.response.SearchMoviesResponse
import com.haraev.main.domain.repository.SearchRepository
import io.reactivex.Single

class SearchRepositoryImpl(
    private val mainService: MainService
) : SearchRepository {

    override fun getMovies(
        query: String,
        page: Int
    ): Single<SearchMoviesResponse> {
        return mainService.getMovies(
            query = query,
            page = page
        )
    }

    override fun getMovieDetails(
        movieId: Int
    ): Single<MovieDetailsResponse> {
        return mainService.getMovieDetails(
            movieId = movieId
        )
    }
}