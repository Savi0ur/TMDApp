package com.haraev.authentication.di.module.usePin

import com.haraev.authentication.data.AccountRepositoryImpl
import com.haraev.authentication.data.PinCodeRepositoryImpl
import com.haraev.authentication.data.api.AccountService
import com.haraev.authentication.domain.repository.AccountRepository
import com.haraev.authentication.domain.repository.PinCodeRepository
import com.haraev.authentication.domain.usecase.UsePinCodeUseCase
import com.haraev.core.common.EXTENDED_TMDB_BASE_URL
import com.haraev.core.data.LocalUserDataSource
import com.haraev.core.di.qualifier.HttpClientQualifier
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.moshi.MoshiConverterFactory

@Module
class UsePinCodeModule {

    @Provides
    fun provideAccountService(
        @HttpClientQualifier(withAuthenticator = true)
        client: OkHttpClient
    ): AccountService =
        Retrofit.Builder()
            .baseUrl(EXTENDED_TMDB_BASE_URL)
            .client(client)
            .addConverterFactory(MoshiConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build()
            .create(AccountService::class.java)

    @Provides
    fun providePinCodeRepository(
        localUserDataSource: LocalUserDataSource
    ): PinCodeRepository {
        return PinCodeRepositoryImpl(localUserDataSource)
    }

    @Provides
    fun provideAccountRepository(
        accountService: AccountService,
        localUserDataSource: LocalUserDataSource
    ): AccountRepository {
        return AccountRepositoryImpl(accountService, localUserDataSource)
    }

    @Provides
    fun provideUsePinCodeUseCase(
        pinCodeRepository: PinCodeRepository,
        accountRepository: AccountRepository
    ): UsePinCodeUseCase {
        return UsePinCodeUseCase(pinCodeRepository, accountRepository)
    }
}