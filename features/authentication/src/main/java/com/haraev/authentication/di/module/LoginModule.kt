package com.haraev.authentication.di.module

import com.haraev.authentication.data.LoginRepositoryImpl
import com.haraev.authentication.data.api.LoginService
import com.haraev.authentication.domain.repository.LoginRepository
import com.haraev.authentication.domain.usecase.LoginUseCase
import com.haraev.authentication.presentation.LoginViewModelFactory
import com.haraev.core.data.SessionLocalDataSource
import dagger.Module
import dagger.Provides

@Module
class LoginModule {

    @Provides
    fun provideLoginRepository(
        loginService: LoginService,
        sessionLocalDataSource: SessionLocalDataSource
    ): LoginRepository {
        return LoginRepositoryImpl(loginService, sessionLocalDataSource)
    }

    @Provides
    fun provideLoginUseCase(loginRepository: LoginRepository): LoginUseCase {
        return LoginUseCase(loginRepository)
    }

    @Provides
    fun provideLoginViewModelFactory(loginUseCase: LoginUseCase): LoginViewModelFactory {
        return LoginViewModelFactory(loginUseCase)
    }
}