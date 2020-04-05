package com.haraev.main.presentation.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.haraev.core.common.ThreadScheduler
import com.haraev.main.domain.usecase.ProfileUseCase
import javax.inject.Inject

class ProfileViewModelFactory @Inject constructor(
    private val profileUseCase: ProfileUseCase,
    private val scheduler: ThreadScheduler
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass != ProfileViewModel::class.java) {
            throw IllegalArgumentException("Unknown ViewModel class")
        }
        return ProfileViewModel(profileUseCase, scheduler) as T
    }
}