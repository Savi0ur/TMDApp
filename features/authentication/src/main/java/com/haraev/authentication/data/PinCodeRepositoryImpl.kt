package com.haraev.authentication.data

import com.haraev.authentication.domain.repository.PinCodeRepository
import com.haraev.core.data.LocalUserDataSource
import io.reactivex.Completable
import io.reactivex.Single

class PinCodeRepositoryImpl(
    private val localUserDataSource: LocalUserDataSource
) : PinCodeRepository {

    override fun savePinCode(pinCode: String): Completable {
        return Completable.create {
            localUserDataSource.userPin = pinCode
            it.onComplete()
        }
    }

    override fun getPinCode(): Single<String> {
        return Single.just(requireNotNull(localUserDataSource.userPin))
    }

    override fun saveBiometricAct(boolean: Boolean): Completable {
        return Completable.create {
            localUserDataSource.biometricAct = boolean
            it.onComplete()
        }
    }

    override fun getBiometricAct(): Single<Boolean> {
        return Single.just(requireNotNull(localUserDataSource.biometricAct))
    }
}