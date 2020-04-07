package com.haraev.main.data

import com.haraev.core.data.SessionLocalDataSource
import com.haraev.main.data.api.MainService
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
            language = "en",
            query = query,
            page = page
        ).flatMap {
            Single.just(it.body())
        }
    }
}