package com.haraev.main.di.module.profile

import com.haraev.core.data.SessionLocalDataSource
import com.haraev.main.data.ProfileRepositoryImpl
import com.haraev.main.data.api.MainService
import com.haraev.main.domain.repository.ProfileRepository
import com.haraev.main.domain.usecase.ProfileUseCase
import dagger.Module
import dagger.Provides

@Module
class ProfileModule {

    @Provides
    fun provideProfileRepository(
        mainService: MainService,
        sessionLocalDataSource: SessionLocalDataSource
    ) : ProfileRepository {
        return ProfileRepositoryImpl(
            mainService,
            sessionLocalDataSource
        )
    }

    @Provides
    fun provideProfileUseCase(
        profileRepository: ProfileRepository
    ) : ProfileUseCase {
        return ProfileUseCase(profileRepository)
    }
}