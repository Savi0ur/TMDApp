package com.haraev.core.data.exception

import java.io.IOException

data class NetworkException(
    val statusCode: Int,
    val statusMessage : String
) : IOException("statusCode: $statusCode, statusMessage: $statusMessage")

/**
 * Перечисления статус кодов ответов
 * https://www.themoviedb.org/documentation/api/status-codes
 */
enum class NetworkExceptionType(val code: Int) {
    INVALID_LOGIN_CREDENTIALS(30),
    EMAIL_NOT_VERIFIED(32)
}