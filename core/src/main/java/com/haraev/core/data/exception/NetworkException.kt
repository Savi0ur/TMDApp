package com.haraev.core.data.exception

import java.io.IOException

open class NetworkException(
    statusCode: Int,
    statusMessage: String
) : IOException("statusCode: $statusCode, statusMessage: $statusMessage")

data class InvalidLoginCredentialsException(
    val exceptionMessage: String
) : NetworkException(NetworkExceptionType.INVALID_LOGIN_CREDENTIALS.code, exceptionMessage)

data class EmailNotVerifiedException(
    val exceptionMessage: String
) : NetworkException(NetworkExceptionType.EMAIL_NOT_VERIFIED.code, exceptionMessage)

/**
 * Перечисления статус кодов ответов
 * https://www.themoviedb.org/documentation/api/status-codes
 */
enum class NetworkExceptionType(val code: Int) {
    INVALID_LOGIN_CREDENTIALS(30),
    EMAIL_NOT_VERIFIED(32)
}