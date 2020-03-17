package com.haraev.core.data.api.interceptors

import com.haraev.core.TMDB_API_KEY
import okhttp3.Interceptor
import okhttp3.Response

/**
 * Интерсептор для добавления API ключа ко всем сетевым запросам
 */
class ApiKeyInterceptor : Interceptor {

    companion object {
        private const val API_KEY_QUERY_PARAMETER = "api_key"
    }

    override fun intercept(chain: Interceptor.Chain): Response {
        val newUrl = chain.request().url
            .newBuilder()
            .addQueryParameter(API_KEY_QUERY_PARAMETER, TMDB_API_KEY)
            .build()

        val request = chain.request().newBuilder().url(newUrl).build()

        return chain.proceed(request)
    }
}