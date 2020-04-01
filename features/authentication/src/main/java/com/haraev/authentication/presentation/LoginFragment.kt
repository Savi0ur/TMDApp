package com.haraev.authentication.presentation

import android.content.Context
import android.os.Bundle
import android.view.View
import android.view.inputmethod.EditorInfo
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.viewModels
import androidx.lifecycle.observe
import com.haraev.authentication.R
import com.haraev.authentication.di.component.LoginComponent
import com.haraev.core.di.provider.CoreComponentProvider
import com.haraev.core.navigation.NavigationActivity
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
        viewModel.uiCommand.observe(viewLifecycleOwner, ::handleViewCommand)
        viewModel.uiState.observe(viewLifecycleOwner, ::handleViewState)
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

    private fun handleViewCommand(viewCommand: LoginViewCommand) {
        when (viewCommand) {
            is LoginViewCommand.NavigateToNextScreen -> {
                /**
                 * Заглушка
                 * TODO Переход на основной экран приложения после успешного логина
                 */
//                requireActivity().finish()
                (requireActivity() as NavigationActivity).navigateToMainScreen()
            }
        }
    }

    private fun handleViewState(viewState: LoginViewState) {
        when (viewState.progressBarVisibility) {
            true -> login_progress_bar.visibility = View.VISIBLE
            false -> login_progress_bar.visibility = View.GONE
        }

        login_enter_button.isEnabled = viewState.enterButtonEnable

        login_login_text_input_layout.isEnabled = viewState.loginAndPasswordFieldsEnable
        login_password_text_input_layout.isEnabled = viewState.loginAndPasswordFieldsEnable

        if (viewState.errorMessage != null) {
            login_error_text_view.setText(viewState.errorMessage)
        } else {
            login_error_text_view.text = null
        }
    }
}