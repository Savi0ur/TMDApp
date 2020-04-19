package com.haraev.tmdapp.di.module

import androidx.lifecycle.ViewModel
import com.haraev.core.di.module.ViewModelModule
import com.haraev.core.ui.ViewModelKey
import com.haraev.tmdapp.ui.MainViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module(includes = [ViewModelModule::class])
abstract class MainViewModelModule {

    @Binds
    @IntoMap
    @ViewModelKey(MainViewModel::class)
    abstract fun provideMainViewModel(viewModel: MainViewModel): ViewModel
}