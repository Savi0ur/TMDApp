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
        return Single.just(localUserDataSource.userPin)
    }
}