package com.haraev.main.data.model.request

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class MarkAsFavoriteBody(

    @Json(name = "media_type")
    val mediaType: String = "movie",

    @Json(name = "movie_id")
    val mediaId: Int,

    @Json(name = "favorite")
    val favorite: Boolean
)