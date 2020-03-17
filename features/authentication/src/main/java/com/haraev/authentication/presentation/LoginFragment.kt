package com.haraev.authentication.presentation

import android.content.Context
import android.os.Bundle
import android.view.View
import android.view.inputmethod.EditorInfo
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.haraev.authentication.R
import com.haraev.authentication.di.component.LoginComponent
import com.haraev.core.di.provider.CoreComponentProvider
import com.haraev.core.ui.BaseFragment
import kotlinx.android.synthetic.main.fragment_login.*
import javax.inject.Inject

class LoginFragment : BaseFragment(R.layout.fragment_login) {

    @Inject
    lateinit var viewModelFactory: LoginViewModelFactory

    private val viewModel: LoginViewModel by viewModels { viewModelFactory }

    override fun onAttach(context: Context) {
        LoginComponent.Builder.build((requireActivity().application as CoreComponentProvider).getCoreComponent())
            .inject(this)

        super.onAttach(context)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        observeViewModel()
        initView()
    }

    private fun observeViewModel() {
        viewModel.uiCommand.observe(
            viewLifecycleOwner,
            Observer { loginViewCommand ->
                when (loginViewCommand) {
                    is LoginViewCommand.ChangeProgressBarVisibility -> {
                        login_progress_bar.visibility =
                            if (loginViewCommand.visible) View.VISIBLE else View.INVISIBLE
                    }
                    is LoginViewCommand.ChangeEnterButtonEnable -> {
                        login_enter_button.isEnabled = loginViewCommand.enabled
                    }
                    is LoginViewCommand.ChangeLoginPasswordFieldsEnable -> {
                        login_login_text_input_edit_text.isEnabled = loginViewCommand.enabled
                        login_password_text_input_edit_text.isEnabled = loginViewCommand.enabled
                    }
                    is LoginViewCommand.ShowErrorMessage -> {
                        login_error_text_view.setText(loginViewCommand.resId)
                    }
                    is LoginViewCommand.NavigateToNextScreen -> {
                        /**
                         * Заглушка
                         * TODO Переход на основной экран приложения после успешного логина
                         */
                        requireActivity().finish()
                    }
                }
            }
        )
    }

    private fun initView() {
        setupLoginTextInputEditText()
        setupPasswordTextInputEditText()
        setupEnterButton()
    }

    private fun setupEnterButton() {
        login_enter_button.setOnClickListener {
            viewModel.enterButtonClicked(
                login_login_text_input_edit_text.text.toString(),
                login_password_text_input_edit_text.text.toString()
            )
            hideKeyboard()
        }
    }

    private fun setupLoginTextInputEditText() {
        login_login_text_input_edit_text.doAfterTextChanged {
            viewModel.loginDataChanged(
                it.toString(),
                login_password_text_input_edit_text.text.toString()
            )
        }
    }

    private fun setupPasswordTextInputEditText() {
        login_password_text_input_edit_text.apply {
            doAfterTextChanged {
                viewModel.loginDataChanged(
                    login_login_text_input_edit_text.text.toString(),
                    it.toString()
                )
            }
            setOnEditorActionListener { _, actionId, _ ->
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    viewModel.enterButtonClicked(
                        login_login_text_input_edit_text.text.toString(),
                        login_password_text_input_edit_text.text.toString()
                    )
                    hideKeyboard()
                }
                false
            }
        }
    }
}