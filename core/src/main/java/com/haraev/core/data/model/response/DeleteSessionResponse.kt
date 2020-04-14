package com.haraev.core.data.model.response

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class DeleteSessionResponse(

    @Json(name = "success")
    val success : Boolean
)