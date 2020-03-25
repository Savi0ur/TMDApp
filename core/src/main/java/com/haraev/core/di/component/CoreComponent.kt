package com.haraev.core.di.component

import android.content.Context
import android.content.SharedPreferences
import com.haraev.core.common.ThreadScheduler
import com.haraev.core.data.SessionLocalDataSource
import com.haraev.core.data.api.LoginService
import com.haraev.core.di.module.CoreDataModule
import com.haraev.core.di.module.CoreNetworkModule
import com.haraev.core.di.module.SharedPreferencesModule
import com.squareup.moshi.Moshi
import dagger.BindsInstance
import dagger.Component
import okhttp3.OkHttpClient
import javax.inject.Named
import javax.inject.Singleton

@Singleton
@Component(modules = [CoreNetworkModule::class, SharedPreferencesModule::class, CoreDataModule::class])
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

    @Named("ClientWithOutAuthenticator")
    fun provideOkHttpClient(): OkHttpClient

    fun provideLoginService(): LoginService

    fun provideMoshi(): Moshi

    fun provideSessionLocalDataSource(): SessionLocalDataSource

    fun provideSharedPreferences(): SharedPreferences

    fun provideThreadScheduler(): ThreadScheduler
}