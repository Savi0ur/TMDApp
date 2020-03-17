package com.haraev.core.data.model.response

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class TokenResponse(
    @Json(name = "success")
    val isSuccess: Boolean?,

    @Json(name = "request_token")
    val requestToken: String?,

    @Json(name = "status_message")
    val statusMessage: String?
)