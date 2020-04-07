package com.haraev.main.data

import com.haraev.main.data.api.MainService
import com.haraev.main.data.model.Movie
import com.haraev.main.data.model.MovieUi
import com.haraev.main.data.model.response.MovieDetailsResponse
import com.haraev.main.domain.repository.SearchRepository
import io.reactivex.Single

class SearchRepositoryImpl(
    private val mainService: MainService
) : SearchRepository {

    override fun getMovies(
        query: String,
        page: Int
    ): Single<List<MovieUi>> {
        return mainService.getMovies(
            language = "ru",
            query = query,
            page = page
        ).flatMap {
            val movies: MutableList<MovieUi> = mutableListOf()
            it.body()?.movies?.forEach { movie ->
                val movieDetails = mainService.getMovieDetails(
                    language = "ru",
                    movieId = movie.serverId
                ).blockingGet().body()
                val movieUi = mapMovieWithDetails(movie, movieDetails)
                movies.add(movieUi)
            }
            return@flatMap Single.just(movies)
        }
    }

    private fun mapMovieWithDetails(
        movie: Movie,
        movieDetailsResponse: MovieDetailsResponse?
    ): MovieUi {
        return with(movie) {
            MovieUi(
                poster_path = poster_path,
                overview = overview,
                releaseDate = releaseDate,
                serverId = serverId,
                originalTitle = originalTitle,
                title = title,
                voteCount = voteCount,
                voteAverage = movie.voteAverage,
                duration = movieDetailsResponse?.duration,
                genres = movieDetailsResponse?.genres
            )
        }
    }
}