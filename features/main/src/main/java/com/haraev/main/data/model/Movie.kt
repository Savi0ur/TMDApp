package com.haraev.main.data.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Movie(

    @Json(name = "poster_path")
    val poster_path : String?,

    @Json(name = "overview")
    val overview : String,

    @Json(name = "release_date")
    val releaseDate : String?,

    @Json(name = "id")
    val serverId : Int,

    @Json(name = "original_title")
    val originalTitle : String,

    @Json(name = "title")
    val title : String,

    @Json(name = "vote_count")
    val voteCount : Int,

    @Json(name = "vote_average")
    val voteAverage : Double

)