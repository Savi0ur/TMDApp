package com.haraev.main.data

import com.haraev.main.data.api.MainService
import com.haraev.main.data.model.response.MovieDetailsResponse
import com.haraev.main.data.model.response.SearchMoviesResponse
import com.haraev.main.domain.repository.MovieRepository
import io.reactivex.Single

class MovieRepositoryImpl(
    private val mainService: MainService
) : MovieRepository {

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