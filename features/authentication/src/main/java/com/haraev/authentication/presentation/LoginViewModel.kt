package com.haraev.authentication.presentation

import androidx.lifecycle.MutableLiveData
import com.haraev.authentication.R
import com.haraev.authentication.domain.usecase.LoginUseCase
import com.haraev.core.aac.EventsQueue
import com.haraev.core.aac.delegate
import com.haraev.core.common.ThreadScheduler
import com.haraev.core.common.scheduleIoToUi
import com.haraev.core.data.exception.NetworkException
import com.haraev.core.data.exception.NetworkExceptionType
import com.haraev.core.ui.BaseViewModel
import javax.inject.Inject

class LoginViewModel @Inject constructor(
    private val loginUseCase: LoginUseCase,
    private val scheduler: ThreadScheduler
) : BaseViewModel() {

    val uiState = MutableLiveData<LoginViewState>(createInitialState())

    private var state: LoginViewState by uiState.delegate()

    val eventsQueue = EventsQueue()

    fun loginDataChanged(login: String, password: String) {
        enterButtonEnable(isLoginValid(login, password))
    }

    fun enterButtonClicked(login: String, password: String) {
        clearErrorMessage()
        showUiStartLoading()
        startLoginProcess(login, password)
    }

    private fun startLoginProcess(login: String, password: String) {
        loginUseCase.login(login, password)
            .scheduleIoToUi(scheduler)
            .doOnTerminate {
                showUiStopLoading()
            }
            .subscribe({
                navigateToNextScreen()
            }, { throwable ->

                if (throwable is NetworkException) {
                    handleNetworkException(throwable)
                } else {
                    showErrorMessage(R.string.login_unknown_error_message)
                }

            })
            .autoDispose()
    }

    private fun isLoginValid(login: String, password: String): Boolean {
        return login.isNotBlank() && password.isNotBlank()
    }

    private fun showUiStartLoading() {
        progressBarVisibility(true)
        loginPasswordEnable(false)
        enterButtonEnable(false)
    }

    private fun showUiStopLoading() {
        progressBarVisibility(false)
        loginPasswordEnable(true)
        enterButtonEnable(true)
    }

    private fun handleNetworkException(networkException: NetworkException) {
        when (networkException.statusCode) {
            NetworkExceptionType.EMAIL_NOT_VERIFIED.code -> showErrorMessage(R.string.wrong_login_or_password)
            NetworkExceptionType.INVALID_LOGIN_CREDENTIALS.code -> showErrorMessage(R.string.wrong_login_or_password)
            else -> showErrorMessage(R.string.login_unknown_error_message)
        }
    }

    private fun navigateToNextScreen() {
        eventsQueue.offer(LoginEvents.NavigateToNextScreen)
    }

    private fun showErrorMessage(messageResId: Int) {
        state = state.copy(errorMessage = messageResId)
    }

    private fun clearErrorMessage() {
        state = state.copy(errorMessage = null)
    }

    private fun progressBarVisibility(visible: Boolean) {
        state = state.copy(progressBarVisibility = visible)
    }

    private fun loginPasswordEnable(enabled: Boolean) {
        state = state.copy(loginAndPasswordFieldsEnable = enabled)
    }

    private fun enterButtonEnable(enabled: Boolean) {
        state = state.copy(enterButtonEnable = enabled)
    }

    private fun createInitialState(): LoginViewState = LoginViewState()
}