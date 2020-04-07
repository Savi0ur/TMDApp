package com.haraev.main.domain.repository

import com.haraev.main.data.model.MovieUi
import com.haraev.main.data.model.response.SearchMoviesResponse
import io.reactivex.Single

interface SearchRepository {

    fun getMovies(
        query: String,
        page: Int
    ): Single<List<MovieUi>>
}