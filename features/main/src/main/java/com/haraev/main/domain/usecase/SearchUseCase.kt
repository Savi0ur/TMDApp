package com.haraev.main.domain.usecase

import com.haraev.main.data.model.response.MovieDetailsResponse
import com.haraev.main.domain.repository.MovieRepository
import io.reactivex.Single
import javax.inject.Inject

class SearchUseCase @Inject constructor(
    private val movieRepository: MovieRepository
) {

    fun getMovies(query: String, page: Int): Single<List<MovieDetailsResponse>> {
        return movieRepository.getMovies(query, page)
            .flattenAsObservable { it.movies }
            .flatMap { movie ->
                movieRepository.getMovieDetails(movie.serverId).toObservable()
            }
            .collect(
                { ArrayList<MovieDetailsResponse>().toMutableList() },
                { list, item -> list.add(item) }
            )
            .map { it.toList() }
    }
}