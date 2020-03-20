package com.haraev.tmdapp.di.module

import com.haraev.core.data.SessionLocalDataSource
import com.haraev.tmdapp.ui.MainViewModelFactory
import dagger.Module

@Module
class MainModule {

    fun provideLoginViewModelFactory(
        sessionLocalDataSource: SessionLocalDataSource
    ): MainViewModelFactory =
        MainViewModelFactory(sessionLocalDataSource)

}