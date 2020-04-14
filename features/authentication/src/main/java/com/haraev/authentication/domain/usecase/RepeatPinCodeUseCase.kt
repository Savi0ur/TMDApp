package com.haraev.authentication.domain.usecase

import com.haraev.authentication.domain.repository.PinCodeRepository
import io.reactivex.Completable
import javax.inject.Inject

class RepeatPinCodeUseCase @Inject constructor(
    private val pinCodeRepository: PinCodeRepository
) {

    fun savePinCode(pinCode: String): Completable =
        pinCodeRepository.savePinCode(pinCode)

    fun saveBiometricAct(boolean: Boolean): Completable =
        pinCodeRepository.saveBiometricAct(boolean)
}