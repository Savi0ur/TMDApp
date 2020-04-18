package com.haraev.main.di.module

import com.haraev.main.presentation.BottomNavigationRouter
import dagger.Module
import dagger.Provides

@Module
class DataModule {

    @Provides
    fun provideNavigationRouter(): BottomNavigationRouter = BottomNavigationRouter
}