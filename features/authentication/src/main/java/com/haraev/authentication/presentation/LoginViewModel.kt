package com.haraev.authentication.presentation

import androidx.lifecycle.MutableLiveData
import com.haraev.authentication.R
import com.haraev.authentication.domain.usecase.LoginUseCase
import com.haraev.core.ui.BaseViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import timber.log.Timber
import javax.inject.Inject

class LoginViewModel @Inject constructor(
    private val loginUseCase: LoginUseCase
) : BaseViewModel() {

    companion object {
        private const val TAG = "LoginViewModel"
    }

    val uiCommand = MutableLiveData<LoginViewCommand>()

    fun loginDataChanged(login: String, password: String) {
        enterButtonEnable(isLoginValid(login, password))
    }

    fun enterButtonClicked(login: String, password: String) {
        showUiStartLoading()
        startLoginProcess(login, password)
    }

    private fun startLoginProcess(login: String, password: String) {
        loginUseCase.login(login, password)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                showUiStopLoading()
            }, { throwable ->
                Timber.tag(TAG).e(throwable)
                showUiStopLoading()
                showErrorMessage(R.string.login_unknown_error_message)
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

    private fun showErrorMessage(messageResId: Int) {
        uiCommand.value = LoginViewCommand.ShowErrorMessage(messageResId)
    }

    private fun progressBarVisibility(visible: Boolean) {
        uiCommand.value = LoginViewCommand.ChangeProgressBarVisibility(visible)
    }

    private fun loginPasswordEnable(enabled: Boolean) {
        uiCommand.value = LoginViewCommand.ChangeLoginPasswordFieldsEnable(enabled)
    }

    private fun enterButtonEnable(enabled: Boolean) {
        uiCommand.value = LoginViewCommand.ChangeEnterButtonEnable(enabled)
    }
}