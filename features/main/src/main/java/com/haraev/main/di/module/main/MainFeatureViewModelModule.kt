package com.haraev.main.di.module.main

import androidx.lifecycle.ViewModel
import com.haraev.core.di.module.ViewModelModule
import com.haraev.core.ui.ViewModelKey
import com.haraev.main.presentation.main.MainFeatureViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module(includes = [ViewModelModule::class])
abstract class MainFeatureViewModelModule {

    @Binds
    @IntoMap
    @ViewModelKey(MainFeatureViewModel::class)
    abstract fun provideMainFeatureViewModel(viewModel: MainFeatureViewModel): ViewModel
}