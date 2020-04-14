package com.haraev.authentication.domain.repository

import io.reactivex.Completable
import io.reactivex.Single

interface PinCodeRepository {

    fun savePinCode(pinCode: String) : Completable

    fun getPinCode() : Single<String>

    fun saveBiometricAct(boolean: Boolean) : Completable

    fun getBiometricAct() : Single<Boolean>
}