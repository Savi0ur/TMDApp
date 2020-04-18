package com.haraev.main.di.module.movieDetails

import androidx.lifecycle.ViewModel
import com.haraev.core.di.module.ViewModelModule
import com.haraev.core.ui.ViewModelKey
import com.haraev.main.presentation.moviedetails.MovieDetailsViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module(includes = [ViewModelModule::class])
abstract class MovieDetailsViewModelModule {

    @Binds
    @IntoMap
    @ViewModelKey(MovieDetailsViewModel::class)
    abstract fun provideMovieDetailsViewModel(viewModel: MovieDetailsViewModel): ViewModel
}