package com.haraev.main.di.module.favorite

import androidx.lifecycle.ViewModel
import com.haraev.core.di.module.ViewModelModule
import com.haraev.core.ui.ViewModelKey
import com.haraev.main.presentation.favorite.FavoriteViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module(includes = [ViewModelModule::class])
abstract class FavoriteViewModelModule {

    @Binds
    @IntoMap
    @ViewModelKey(FavoriteViewModel::class)
    abstract fun provideFavoriteViewModel(viewModel: FavoriteViewModel): ViewModel
}