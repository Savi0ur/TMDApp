package com.haraev.main.data.model.request

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class DeleteSessionBody(

    @Json(name = "session_id")
    val sessionId : String
)