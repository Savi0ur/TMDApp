package com.haraev.core.data.model.response

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ErrorResponse(
    @Json(name = "status_code")
    val statusCode: Int,

    @Json(name = "status_message")
    val statusMessage: String
)