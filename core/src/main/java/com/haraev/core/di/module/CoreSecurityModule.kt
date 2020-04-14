package com.haraev.core.di.module

import android.content.Context
import com.haraev.core.cryptography.Cryptographer
import com.haraev.core.di.provider.SecurityProvider
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class CoreSecurityModule {

    @Provides
    @Singleton
    fun provideCryptographer(context: Context): Cryptographer =
        (context as SecurityProvider).getCryptographer()
}