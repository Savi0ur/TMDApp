package com.haraev.core.di.module

import android.content.Context
import com.haraev.core.cryptography.Cryptographer
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class CoreSecurityModule {

    @Provides
    @Singleton
    fun provideCryptographer(context: Context): Cryptographer {
        val cryptographer = Cryptographer(context.applicationContext)
        cryptographer.register()
        return cryptographer
    }
}