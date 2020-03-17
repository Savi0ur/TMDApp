package com.haraev.core.di.module

import android.content.SharedPreferences
import com.haraev.core.TMDB_BASE_URL
import com.haraev.core.data.SessionLocalDataSource
import com.haraev.core.data.api.interceptors.ApiKeyInterceptor
import com.haraev.core.data.api.SessionAuthenticator
import com.haraev.core.data.api.interceptors.TmdApiLoggingInterceptor
import com.haraev.core.data.api.TokenService
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Named

@Module
class CoreNetworkModule {

    @Provides
    fun provideTokenService(
        @Named("ClientWithOutAuthenticator")
        client: OkHttpClient
    ): TokenService {
        return Retrofit.Builder()
            .baseUrl(TMDB_BASE_URL)
            .client(client)
            .addConverterFactory(MoshiConverterFactory.create())
            .build()
            .create(TokenService::class.java)
    }

    @Provides
    fun provideApiKeyInterceptor(): ApiKeyInterceptor =
        ApiKeyInterceptor()

    @Provides
    fun provideTmdApiLoggingInterceptor(): TmdApiLoggingInterceptor =
        TmdApiLoggingInterceptor()

    @Provides
    fun provideSessionAuthenticator(
        sessionLocalDataSource: SessionLocalDataSource,
        tokenService: TokenService
    ): SessionAuthenticator =
        SessionAuthenticator(
            sessionLocalDataSource,
            tokenService
        )


    @Named("ClientWithOutAuthenticator")
    @Provides
    fun provideOkHttpClient(
        apiKeyInterceptor: ApiKeyInterceptor,
        tmdApiLoggingInterceptor: TmdApiLoggingInterceptor
    ): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(apiKeyInterceptor)
            .addInterceptor(tmdApiLoggingInterceptor)
            .build()
    }


    @Named("ClientWithAuthenticator")
    @Provides
    fun provideOkHttpClientWithAuthenticator(
        apiKeyInterceptor: ApiKeyInterceptor,
        tmdApiLoggingInterceptor: TmdApiLoggingInterceptor,
        authenticator: SessionAuthenticator
    ): OkHttpClient {
        return OkHttpClient.Builder()
            .authenticator(authenticator)
            .addInterceptor(apiKeyInterceptor)
            .addInterceptor(tmdApiLoggingInterceptor)
            .build()
    }

    @Provides
    fun provideSessionLocalDataSource(
        sharedPreferences: SharedPreferences
    ): SessionLocalDataSource = SessionLocalDataSource(sharedPreferences)
}