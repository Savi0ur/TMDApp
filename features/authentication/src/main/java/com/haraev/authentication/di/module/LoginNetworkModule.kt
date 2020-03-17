package com.haraev.authentication.di.module

import com.haraev.authentication.data.api.LoginService
import com.haraev.core.TMDB_BASE_URL
import com.haraev.core.di.scope.FeatureModuleScope
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Named

@Module
class LoginNetworkModule {

    @Provides
    @FeatureModuleScope
    fun provideLoginService(
        @Named("ClientWithOutAuthenticator")
        client: OkHttpClient
    ) : LoginService {
        return Retrofit.Builder()
            .baseUrl(TMDB_BASE_URL)
            .client(client)
            .addConverterFactory(MoshiConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build()
            .create(LoginService::class.java)
    }

}