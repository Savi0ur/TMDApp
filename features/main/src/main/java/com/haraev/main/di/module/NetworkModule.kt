package com.haraev.main.di.module

import com.haraev.core.common.TMDB_BASE_URL
import com.haraev.main.data.api.MainService
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Named

@Module
class NetworkModule {

    @Provides
    fun provideMainService(
        @Named("ClientWithAuthenticator")
        client: OkHttpClient
    ) : MainService =
        Retrofit.Builder()
            .baseUrl(TMDB_BASE_URL)
            .client(client)
            .addConverterFactory(MoshiConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build()
            .create(MainService::class.java)

}