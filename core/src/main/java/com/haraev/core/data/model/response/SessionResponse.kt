package com.haraev.core.data.model.response

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class SessionResponse(
    @Json(name = "success")
    val isSuccess: Boolean?,

    @Json(name = "session_id")
    val sessionId: String?,

    @Json(name = "status_message")
    val statusMessage: String?
)