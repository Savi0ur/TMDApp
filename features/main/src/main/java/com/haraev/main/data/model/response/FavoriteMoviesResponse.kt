package com.haraev.main.data.model.response

import com.haraev.main.data.model.Movie
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class FavoriteMoviesResponse (

    @Json(name = "page")
    val page: Int,

    @Json(name = "results")
    val movies : List<Movie>,

    @Json(name = "total_result")
    val totalResults : Int?,

    @Json(name = "total_pages")
    val totalPages : Int
)