package com.haraev.main.data.model.response

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class AccountDetailsResponse(

    @Json(name = "name")
    val name : String,

    @Json(name = "username")
    val username : String
)