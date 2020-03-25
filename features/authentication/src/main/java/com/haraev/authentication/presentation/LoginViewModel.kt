package com.haraev.authentication.presentation

import androidx.lifecycle.MutableLiveData
import com.haraev.authentication.R
import com.haraev.authentication.domain.usecase.LoginUseCase
import com.haraev.core.common.ThreadScheduler
import com.haraev.core.common.scheduleIoToUi
import com.haraev.core.data.exception.NetworkException
import com.haraev.core.data.exception.NetworkExceptionType
import com.haraev.core.ui.BaseViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import timber.log.Timber
import javax.inject.Inject

class LoginViewModel @Inject constructor(
    private val loginUseCase: LoginUseCase,
    private val scheduler: ThreadScheduler
) : BaseViewModel() {

    companion object {
        private const val TAG = "LoginViewModel"
    }

    val uiCommand = MutableLiveData<LoginViewCommand>()

    val uiState = MutableLiveData<LoginViewState>()

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
            .subscribe({
                showUiStopLoading()
                navigateToNextScreen()
            }, { throwable ->
                showUiStopLoading()

                if (throwable is NetworkException) {
                    handleNetworkException(throwable)
                } else {
                    showErrorMessage(R.string.login_unknown_error_message)
                    Timber.tag(TAG).e(throwable)
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
        uiCommand.value = LoginViewCommand.NavigateToNextScreen
    }

    private fun showErrorMessage(messageResId: Int) {
        uiState.value = getCurrentViewState().copy(errorMessage = messageResId)
    }

    private fun clearErrorMessage() {
        uiState.value = getCurrentViewState().copy(errorMessage = null)
    }

    private fun progressBarVisibility(visible: Boolean) {
        uiState.value = getCurrentViewState().copy(progressBarVisibility = visible)
    }

    private fun loginPasswordEnable(enabled: Boolean) {
        uiState.value = getCurrentViewState().copy(loginAndPasswordFieldsEnable = enabled)
    }

    private fun enterButtonEnable(enabled: Boolean) {
        uiState.value = getCurrentViewState().copy(enterButtonEnable = enabled)
    }

    private fun getCurrentViewState(): LoginViewState {
        val loginViewState = uiState.value
        if (loginViewState != null) {
            return loginViewState
        } else {
            return LoginViewState()
        }
    }
}