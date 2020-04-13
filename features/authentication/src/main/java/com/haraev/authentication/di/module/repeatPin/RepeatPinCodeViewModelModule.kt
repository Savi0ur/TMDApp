package com.haraev.authentication.di.module.repeatPin

import androidx.lifecycle.ViewModel
import com.haraev.authentication.presentation.repeatPin.RepeatPinCodeViewModel
import com.haraev.core.di.module.ViewModelModule
import com.haraev.core.ui.ViewModelKey
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module(includes = [ViewModelModule::class])
abstract class RepeatPinCodeViewModelModule {

    @Binds
    @IntoMap
    @ViewModelKey(RepeatPinCodeViewModel::class)
    abstract fun provideMakePinViewModel(viewModel: RepeatPinCodeViewModel): ViewModel
}