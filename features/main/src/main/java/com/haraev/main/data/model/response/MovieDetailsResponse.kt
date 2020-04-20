package com.haraev.main.data.model.response

import com.haraev.database.entity.MovieDb
import com.haraev.main.data.model.Genre
import com.haraev.main.data.model.Movie
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class MovieDetailsResponse(

    @Json(name = "genres")
    val genres: List<Genre>,

    @Json(name = "runtime")
    val duration: Int?,

    @Json(name = "poster_path")
    val posterPath: String?,

    @Json(name = "overview")
    val overview: String,

    @Json(name = "release_date")
    val releaseDate: String?,

    @Json(name = "id")
    val serverId: Int,

    @Json(name = "original_title")
    val originalTitle: String,

    @Json(name = "title")
    val title: String,

    @Json(name = "vote_count")
    val voteCount: Int,

    @Json(name = "vote_average")
    val voteAverage: Double
) {

    fun convertToDB(): MovieDb {
        return MovieDb(
            duration = duration,
            posterPath = posterPath,
            overview = overview,
            releaseDate = releaseDate,
            serverId = serverId,
            originalTitle = originalTitle,
            title = title,
            voteCount = voteCount,
            voteAverage = voteAverage
        )
    }

    fun convertToMovie(): Movie {
        return Movie(
            posterPath = posterPath,
            overview = overview,
            releaseDate = releaseDate,
            serverId = serverId,
            originalTitle = originalTitle,
            title = title,
            voteCount = voteCount,
            voteAverage = voteAverage
        )
    }
}

fun MovieDb.convertToDomain(): MovieDetailsResponse {
    return MovieDetailsResponse(
        genres = emptyList(),
        duration = duration,
        posterPath = posterPath,
        overview = overview,
        releaseDate = releaseDate,
        serverId = serverId,
        originalTitle = originalTitle,
        title = title,
        voteCount = voteCount,
        voteAverage = voteAverage
    )
}
