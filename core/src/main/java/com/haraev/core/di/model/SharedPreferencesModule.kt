package com.haraev.core.di.model

import android.content.Context
import android.content.SharedPreferences
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class SharedPreferencesModule {

    @Provides
    @Singleton
    fun provideSharedPreferences(context: Context, name: String): SharedPreferences =
        context.applicationContext.getSharedPreferences(name, Context.MODE_PRIVATE)
}