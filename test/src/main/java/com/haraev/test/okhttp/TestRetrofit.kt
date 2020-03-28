package com.haraev.test.okhttp

import com.haraev.core.data.api.ErrorHandlingInterceptor
import com.squareup.moshi.Moshi
import okhttp3.HttpUrl
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.moshi.MoshiConverterFactory

fun getTestRetrofit(baseUrl: HttpUrl): Retrofit {

    val moshi = Moshi.Builder().build()

    val errorInterceptor = ErrorHandlingInterceptor(moshi)

    val client = OkHttpClient.Builder()
        .addInterceptor(errorInterceptor)
        .build()

    return Retrofit.Builder()
        .baseUrl(baseUrl)
        .client(client)
        .addConverterFactory(MoshiConverterFactory.create())
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        .build()
}