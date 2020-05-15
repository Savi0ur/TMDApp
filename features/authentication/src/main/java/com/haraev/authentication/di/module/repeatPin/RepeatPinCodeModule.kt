package com.haraev.authentication.di.module.repeatPin

import com.haraev.authentication.data.PinCodeRepositoryImpl
import com.haraev.authentication.domain.repository.PinCodeRepository
import com.haraev.authentication.domain.usecase.RepeatPinCodeUseCase
import com.haraev.core.data.LocalUserDataSource
import dagger.Module
import dagger.Provides

@Module
class RepeatPinCodeModule {

    @Provides
    fun providePinCodeRepository(
        localUserDataSource: LocalUserDataSource
    ): PinCodeRepository {
        return PinCodeRepositoryImpl(localUserDataSource)
    }

    @Provides
    fun provideRepeatPinCodeUseCase(pinCodeRepository: PinCodeRepository): RepeatPinCodeUseCase {
        return RepeatPinCodeUseCase(pinCodeRepository)
    }
}