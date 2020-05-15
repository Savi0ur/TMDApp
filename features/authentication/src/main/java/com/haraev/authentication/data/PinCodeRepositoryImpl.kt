package com.haraev.authentication.data

import com.haraev.authentication.domain.repository.PinCodeRepository
import com.haraev.core.data.LocalUserDataSource
import io.reactivex.Completable
import io.reactivex.Single

class PinCodeRepositoryImpl(
    private val localUserDataSource: LocalUserDataSource
) : PinCodeRepository {

    override fun savePinCodeHash(pinCodeHash: String): Completable {
        return Completable.fromAction {
            localUserDataSource.userPinCodeHash = pinCodeHash
        }
    }

    override fun getPinCode(): Single<String> {
        return Single.just(requireNotNull(localUserDataSource.userPinCodeHash))
    }

    override fun saveBiometricAct(boolean: Boolean): Completable {
        return Completable.fromAction {
            localUserDataSource.biometricAct = boolean
        }
    }

    override fun getBiometricAct(): Single<Boolean> {
        return Single.just(requireNotNull(localUserDataSource.biometricAct))
    }
}