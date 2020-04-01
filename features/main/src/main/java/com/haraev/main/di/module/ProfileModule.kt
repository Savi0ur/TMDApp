package com.haraev.main.di.module

import com.haraev.core.common.ThreadScheduler
import com.haraev.core.data.SessionLocalDataSource
import com.haraev.main.data.ProfileRepositoryImpl
import com.haraev.main.data.api.MainService
import com.haraev.main.domain.repository.ProfileRepository
import com.haraev.main.domain.usecase.ProfileUseCase
import com.haraev.main.presentation.profile.ProfileViewModelFactory
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

    @Provides
    fun provideProfileViewModelFactory(
        profileUseCase: ProfileUseCase,
        threadScheduler: ThreadScheduler
    ) : ProfileViewModelFactory {
        return ProfileViewModelFactory(
            profileUseCase,
            threadScheduler
        )
    }
}