package com.haraev.core.data.exception

import java.io.IOException

data class ResponseException(
    private val exceptionMessage : String = "Response failed"
) : IOException(exceptionMessage)

