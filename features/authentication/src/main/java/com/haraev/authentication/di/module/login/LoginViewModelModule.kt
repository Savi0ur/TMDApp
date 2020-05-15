package com.haraev.authentication.di.module.login

import androidx.lifecycle.ViewModel
import com.haraev.authentication.presentation.login.LoginViewModel
import com.haraev.core.di.module.ViewModelModule
import com.haraev.core.ui.ViewModelKey
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module(includes = [ViewModelModule::class])
abstract class LoginViewModelModule {

    @Binds
    @IntoMap
    @ViewModelKey(LoginViewModel::class)
    abstract fun provideLoginViewModel(viewModel: LoginViewModel): ViewModel
}