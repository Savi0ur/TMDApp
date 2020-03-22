package com.haraev.core.data.model.request

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class SessionBody(
    @Json(name = "request_token")
    val requestToken: String
)