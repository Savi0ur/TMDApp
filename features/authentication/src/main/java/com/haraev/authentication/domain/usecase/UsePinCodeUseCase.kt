package com.haraev.authentication.domain.usecase

import com.haraev.authentication.domain.repository.PinCodeRepository
import io.reactivex.Single
import javax.inject.Inject

class UsePinCodeUseCase @Inject constructor(
    private val pinCodeRepository: PinCodeRepository
) {

    fun getPinCode() : Single<Int> =
        pinCodeRepository.getPinCode()
            .map { it.toInt() }

    fun getBiometricAct() : Single<Boolean> =
        pinCodeRepository.getBiometricAct()
}