package com.haraev.core.di.module

import androidx.lifecycle.ViewModelProvider
import com.haraev.core.ui.ViewModelFactory
import dagger.Binds
import dagger.Module

@Module
abstract class ViewModelModule {
    @Binds
    abstract fun bindViewModelFactory(factory: ViewModelFactory): ViewModelProvider.Factory
}