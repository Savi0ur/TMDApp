package com.haraev.core.di.model

import android.content.SharedPreferences
import com.haraev.core.data.SessionLocalDataSource
import com.haraev.core.data.api.interceptors.ApiKeyInterceptor
import com.haraev.core.data.api.interceptors.TmdApiLoggingInterceptor
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import javax.inject.Named

@Module
class CoreNetworkModule {

    @Provides
    fun provideApiKeyInterceptor(): ApiKeyInterceptor =
        ApiKeyInterceptor()

    @Provides
    fun provideTmdApiLoggingInterceptor(): TmdApiLoggingInterceptor =
        TmdApiLoggingInterceptor()

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

    @Provides
    fun provideSessionLocalDataSource(
        sharedPreferences: SharedPreferences
    ): SessionLocalDataSource = SessionLocalDataSource(sharedPreferences)
}