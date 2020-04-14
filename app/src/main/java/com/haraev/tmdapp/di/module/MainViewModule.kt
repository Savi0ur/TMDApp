package com.haraev.tmdapp.di.module

import android.content.Context
import com.scottyab.rootbeer.RootBeer
import dagger.Module
import dagger.Provides

@Module
class MainViewModule {

    @Provides
    fun provideRootBeer(context: Context): RootBeer =
        RootBeer(context)
}