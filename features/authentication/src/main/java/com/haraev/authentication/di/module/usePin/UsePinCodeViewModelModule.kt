package com.haraev.authentication.di.module.usePin

import androidx.lifecycle.ViewModel
import com.haraev.authentication.presentation.repeatPin.RepeatPinCodeViewModel
import com.haraev.authentication.presentation.usePin.UsePinCodeViewModel
import com.haraev.core.di.module.ViewModelModule
import com.haraev.core.ui.ViewModelKey
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module(includes = [ViewModelModule::class])
abstract class UsePinCodeViewModelModule {

    @Binds
    @IntoMap
    @ViewModelKey(UsePinCodeViewModel::class)
    abstract fun provideUsePinCodeViewModel(viewModel: UsePinCodeViewModel): ViewModel
}