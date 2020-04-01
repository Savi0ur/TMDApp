package com.haraev.main.domain.usecase

import com.haraev.main.data.model.response.AccountDetailsResponse
import com.haraev.main.domain.repository.ProfileRepository
import io.reactivex.Completable
import io.reactivex.Single
import javax.inject.Inject

class ProfileUseCase @Inject constructor(private val profileRepository: ProfileRepository) {

    fun getAccountDetails() : Single<AccountDetailsResponse> =
        profileRepository.getAccountDetails()

    fun logout() : Completable =
        profileRepository.logout()
}