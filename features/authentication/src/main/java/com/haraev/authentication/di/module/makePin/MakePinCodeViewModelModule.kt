package com.haraev.authentication.di.module.makePin

import androidx.lifecycle.ViewModel
import com.haraev.authentication.presentation.makePin.MakePinCodeViewModel
import com.haraev.core.di.module.ViewModelModule
import com.haraev.core.ui.ViewModelKey
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module(includes = [ViewModelModule::class])
abstract class MakePinCodeViewModelModule {

    @Binds
    @IntoMap
    @ViewModelKey(MakePinCodeViewModel::class)
    abstract fun provideMakePinViewModel(viewModel: MakePinCodeViewModel): ViewModel
}