package com.haraev.main.domain.usecase

import com.haraev.main.data.model.response.MovieDetailsResponse
import com.haraev.main.data.model.response.SearchMoviesResponse
import com.haraev.main.domain.repository.SearchRepository
import io.reactivex.Single
import javax.inject.Inject

class SearchUseCase @Inject constructor(
    private val searchRepository: SearchRepository
) {

    fun getMovies(query: String, page: Int): Single<MutableList<MovieDetailsResponse>> =
        searchRepository.getMovies(query, page)
            .flattenAsObservable { it.movies }
            .flatMap { movie ->
                searchRepository
                    .getMovieDetails(movie.serverId)
                    .toObservable()
            }
            .collect(
                { ArrayList<MovieDetailsResponse>().toMutableList() },
                { list, item -> list.add(item) }
            )
}