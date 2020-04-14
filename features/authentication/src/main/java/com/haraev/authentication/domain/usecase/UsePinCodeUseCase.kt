package com.haraev.authentication.domain.usecase

import com.haraev.authentication.domain.repository.AccountRepository
import com.haraev.authentication.domain.repository.PinCodeRepository
import com.haraev.core.data.model.response.AccountDetailsResponse
import io.reactivex.Completable
import io.reactivex.Single
import javax.inject.Inject

class UsePinCodeUseCase @Inject constructor(
    private val pinCodeRepository: PinCodeRepository,
    private val accountRepository: AccountRepository
) {

    fun getPinCode() : Single<String> =
        pinCodeRepository.getPinCode()

    fun getBiometricAct() : Single<Boolean> =
        pinCodeRepository.getBiometricAct()

    fun logout() : Completable =
        accountRepository.logout()

    fun getAccountDetails() : Single<AccountDetailsResponse> =
        accountRepository.getAccountDetails()
}