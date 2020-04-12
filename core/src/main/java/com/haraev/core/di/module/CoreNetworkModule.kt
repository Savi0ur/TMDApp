package com.haraev.core.di.module

import android.util.Log
import com.haraev.core.common.TMDB_API_KEY
import com.haraev.core.common.EXTENDED_TMDB_BASE_URL
import com.haraev.core.common.TMDB_BASE_URL
import com.haraev.core.data.SessionLocalDataSource
import com.haraev.core.data.api.ErrorHandlingInterceptor
import com.haraev.core.data.api.LoginService
import com.haraev.core.data.api.SessionAuthenticator
import com.haraev.core.di.qualifier.HttpClientQualifier
import com.ihsanbal.logging.Level
import com.ihsanbal.logging.LoggingInterceptor
import com.squareup.moshi.Moshi
import dagger.Module
import dagger.Provides
import okhttp3.CertificatePinner
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.moshi.MoshiConverterFactory

@Module
class CoreNetworkModule {

    @Provides
    fun provideErrorHandlingInterceptor(
        moshi: Moshi
    ): ErrorHandlingInterceptor =
        ErrorHandlingInterceptor(moshi)


    @Provides
    fun provideLoggingInterceptor(): LoggingInterceptor =
        LoggingInterceptor.Builder()
            .setLevel(Level.BASIC)
            .log(Log.VERBOSE)
            .addQueryParam(
                API_KEY_QUERY_PARAMETER,
                TMDB_API_KEY
            )
            .build()

    @Provides
    fun provideSessionAuthenticator(
        sessionLocalDataSource: SessionLocalDataSource,
        loginService: LoginService
    ): SessionAuthenticator =
        SessionAuthenticator(
            sessionLocalDataSource,
            loginService
        )

    @Provides
    fun provideCertificatePinner(): CertificatePinner {
        return CertificatePinner.Builder()
            .add(TMDB_BASE_URL, TMDB_API_SSL_PIN)
            .build()
    }

    @HttpClientQualifier(withAuthenticator = false)
    @Provides
    fun provideOkHttpClient(
        errorHandlingInterceptor: ErrorHandlingInterceptor,
        loggingInterceptor: LoggingInterceptor,
        certificatePinner: CertificatePinner
    ): OkHttpClient =
        OkHttpClient.Builder()
            .addInterceptor(errorHandlingInterceptor)
            .addInterceptor(loggingInterceptor)
            .certificatePinner(certificatePinner)
            .build()

    @HttpClientQualifier(withAuthenticator = true)
    @Provides
    fun provideOkHttpClientWithAuthenticator(
        authenticator: SessionAuthenticator,
        errorHandlingInterceptor: ErrorHandlingInterceptor,
        loggingInterceptor: LoggingInterceptor,
        certificatePinner: CertificatePinner
    ): OkHttpClient =
        OkHttpClient.Builder()
            .authenticator(authenticator)
            .addInterceptor(errorHandlingInterceptor)
            .addInterceptor(loggingInterceptor)
            .certificatePinner(certificatePinner)
            .build()

    @Provides
    fun provideLoginService(
        @HttpClientQualifier(withAuthenticator = false)
        client: OkHttpClient
    ): LoginService =
        Retrofit.Builder()
            .baseUrl(EXTENDED_TMDB_BASE_URL)
            .client(client)
            .addConverterFactory(MoshiConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build()
            .create(LoginService::class.java)

    companion object {
        private const val TMDB_API_SSL_PIN = "sha256/HkCBucsA3Tgiby96X7vjb/ojHaE1BrjvZ2+LRdJJd0E="

        private const val API_KEY_QUERY_PARAMETER = "api_key"
    }
}