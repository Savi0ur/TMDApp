package com.haraev.core.data.api

import com.haraev.core.data.exception.EmailNotVerifiedException
import com.haraev.core.data.exception.InvalidLoginCredentialsException
import com.haraev.core.data.exception.NetworkException
import com.haraev.core.data.exception.NetworkExceptionType
import com.haraev.core.data.model.response.ErrorResponse
import com.squareup.moshi.Moshi
import okhttp3.Interceptor
import okhttp3.Response

class ErrorHandlingInterceptor(
    private val moshi: Moshi
) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val response = chain.proceed(request)
        val body = response.body

        if (!response.isSuccessful) {
            body?.let { responseBody ->
                val errorResponse =
                    moshi.adapter(ErrorResponse::class.java)
                        .fromJson(responseBody.string())

                errorResponse?.let { response ->
                    when (response.statusCode) {
                        NetworkExceptionType.INVALID_LOGIN_CREDENTIALS.code -> throw InvalidLoginCredentialsException(response.statusMessage)
                        NetworkExceptionType.EMAIL_NOT_VERIFIED.code -> throw EmailNotVerifiedException(response.statusMessage)
                        else -> throw NetworkException(response.statusCode, response.statusMessage)
                    }
                }
            }
        }

        return response
    }
}