package com.haraev.main.data.model.response

import com.haraev.main.data.model.Genre
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class MovieDetailsResponse(

    @Json(name = "genres")
    val genres: List<Genre>,

    @Json(name = "runtime")
    val duration: Int?
)