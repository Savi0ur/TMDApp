package com.haraev.core.di.component

import android.content.Context
import android.content.SharedPreferences
import com.haraev.core.common.ThreadScheduler
import com.haraev.core.data.LocalUserDataSource
import com.haraev.core.data.api.LoginService
import com.haraev.core.di.module.*
import com.haraev.core.di.qualifier.HttpClientQualifier
import com.haraev.database.Database
import com.squareup.moshi.Moshi
import dagger.BindsInstance
import dagger.Component
import okhttp3.OkHttpClient
import javax.inject.Singleton

@Singleton
@Component(
    modules =
    [
        CoreNetworkModule::class,
        SharedPreferencesModule::class,
        CoreDataModule::class,
        CoreSecurityModule::class,
        DatabaseModule::class
    ]
)
interface CoreComponent {

    @Component.Builder
    interface ComponentBuilder {
        @BindsInstance
        fun context(context: Context): ComponentBuilder

        @BindsInstance
        fun sharedPreferencesName(name: String): ComponentBuilder

        fun build(): CoreComponent
    }

    class Builder private constructor() {

        companion object {

            fun build(context: Context, sharedPreferencesName: String): CoreComponent {

                return DaggerCoreComponent.builder()
                    .sharedPreferencesName(sharedPreferencesName)
                    .context(context)
                    .build()
            }
        }
    }

    @HttpClientQualifier(withAuthenticator = false)
    fun provideOkHttpClientWithoutAuthenticator(): OkHttpClient

    @HttpClientQualifier(withAuthenticator = true)
    fun provideOkHttpClientWithAuthenticator(): OkHttpClient

    fun provideLoginService(): LoginService

    fun provideMoshi(): Moshi

    fun provideSessionLocalDataSource(): LocalUserDataSource

    fun provideSharedPreferences(): SharedPreferences

    fun provideThreadScheduler(): ThreadScheduler

    fun provideContext(): Context

    fun provideDatabase(): Database
}