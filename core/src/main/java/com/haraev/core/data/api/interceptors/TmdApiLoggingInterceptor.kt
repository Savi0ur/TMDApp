package com.haraev.core.data.api.interceptors

import okhttp3.Interceptor
import okhttp3.Response
import timber.log.Timber
import java.lang.Exception
import java.util.concurrent.TimeUnit

class TmdApiLoggingInterceptor : Interceptor {

    companion object {
        private const val TAG = "TmdApiLog"
    }

    override fun intercept(chain: Interceptor.Chain): Response {

        val request = chain.request()

        Timber.tag(TAG).i("Запрос: ${request.method} ${request.url}")

        val response: Response
        try {
            response = chain.proceed(request)
        } catch (e: Exception) {
            Timber.tag(TAG).e("Ошибка выполнения запроса")
            Timber.tag(TAG).e(e)

            throw e
        }

        Timber.tag(TAG).i("Ответ: ${response.code} ${response.request.url}")

        val startTs = System.nanoTime()

        val executionTime = TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - startTs)

        Timber.tag(TAG).i("Время выполнения запроса - $executionTime ms")

        return response
    }
}