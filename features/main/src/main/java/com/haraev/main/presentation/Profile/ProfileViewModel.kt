package com.haraev.main.presentation.profile

import androidx.lifecycle.MutableLiveData
import com.haraev.core.aac.EventsQueue
import com.haraev.core.aac.delegate
import com.haraev.core.common.ThreadScheduler
import com.haraev.core.common.scheduleIoToUi
import com.haraev.core.ui.BaseViewModel
import com.haraev.main.R
import com.haraev.main.domain.usecase.ProfileUseCase
import javax.inject.Inject

class ProfileViewModel @Inject constructor(
    private val profileUseCase: ProfileUseCase,
    private val scheduler: ThreadScheduler
) : BaseViewModel() {

    val uiState = MutableLiveData<ProfileViewState>(createInitialState())
    private var state: ProfileViewState by uiState.delegate()

    val eventsQueue = EventsQueue()

    init {
        loadProfileInfo()
    }

    fun exitButtonClicked() {
        changeProgressBarState(true)
        logout()
    }

   private fun logout() {
        profileUseCase.logout()
            .scheduleIoToUi(scheduler)
            .subscribe({
                changeProgressBarState(false)
                eventsQueue.offer(ProfileEvents.Logout)
            }, {
                changeProgressBarState(false)
                eventsQueue.offer(ProfileEvents.ErrorMessage(R.string.unknown_error_message))
            })
            .autoDispose()
    }

    private fun loadProfileInfo() {
        changeProgressBarState(true)
        profileUseCase.getAccountDetails()
            .scheduleIoToUi(scheduler)
            .subscribe({ accountDetailsResponse ->
                changeProgressBarState(false)
                showProfileInfo(
                    accountDetailsResponse.name,
                    accountDetailsResponse.username
                )
            }, {
                changeProgressBarState(false)
                eventsQueue.offer(ProfileEvents.ErrorMessage(R.string.unknown_error_message))
            })
            .autoDispose()
    }

    private fun showProfileInfo(
        name: String,
        username: String
    ) {
        state = state.copy(
            name = name,
            userName = username
        )
    }

    private fun createInitialState(): ProfileViewState =
        ProfileViewState(
            progressBarVisibility = false,
            name = null,
            userName = null
        )

    private fun changeProgressBarState(visible: Boolean) {
        state = state.copy(progressBarVisibility = visible)
    }
}