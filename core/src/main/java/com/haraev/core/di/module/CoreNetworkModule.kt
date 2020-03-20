package com.haraev.core.di.module

import android.content.SharedPreferences
import android.util.Log
import com.haraev.core.TMDB_API_KEY
import com.haraev.core.TMDB_BASE_URL
import com.haraev.core.data.SessionLocalDataSource
import com.haraev.core.data.api.SessionAuthenticator
import com.haraev.core.data.api.TokenService
import com.ihsanbal.logging.Level
import com.ihsanbal.logging.LoggingInterceptor
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Named

@Module
class CoreNetworkModule {

    @Provides
    fun provideLoggingInterceptor(): LoggingInterceptor {
        return LoggingInterceptor.Builder()
            .setLevel(Level.BASIC)
            .log(Log.VERBOSE)
            .addQueryParam(API_KEY_QUERY_PARAMETER, TMDB_API_KEY)
            .build()
    }

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
        loggingInterceptor: LoggingInterceptor
    ): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .build()
    }


    @Named("ClientWithAuthenticator")
    @Provides
    fun provideOkHttpClientWithAuthenticator(
        authenticator: SessionAuthenticator,
        loggingInterceptor: LoggingInterceptor
    ): OkHttpClient {
        return OkHttpClient.Builder()
            .authenticator(authenticator)
            .addInterceptor(loggingInterceptor)
            .build()
    }

    companion object {
        private const val API_KEY_QUERY_PARAMETER = "api_key"
    }
}