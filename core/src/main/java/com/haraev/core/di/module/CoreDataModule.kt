package com.haraev.core.di.module

import android.content.SharedPreferences
import com.haraev.core.data.SessionLocalDataSource
import dagger.Module
import dagger.Provides

@Module
class CoreDataModule {

    @Provides
    fun provideSessionLocalDataSource(
        sharedPreferences: SharedPreferences
    ): SessionLocalDataSource = SessionLocalDataSource(sharedPreferences)
}