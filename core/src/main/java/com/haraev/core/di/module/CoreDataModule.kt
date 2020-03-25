package com.haraev.core.di.module

import android.content.SharedPreferences
import com.haraev.core.common.ThreadScheduler
import com.haraev.core.data.SessionLocalDataSource
import com.squareup.moshi.Moshi
import dagger.Module
import dagger.Provides

@Module
class CoreDataModule {

    @Provides
    fun provideMoshi(): Moshi = Moshi.Builder().build()

    @Provides
    fun provideSessionLocalDataSource(
        sharedPreferences: SharedPreferences
    ): SessionLocalDataSource = SessionLocalDataSource(sharedPreferences)

    @Provides
    fun provideThreadScheduler(): ThreadScheduler = ThreadScheduler()
}