package com.haraev.authentication.presentation

import android.os.Bundle
import android.view.View
import android.view.inputmethod.EditorInfo
import androidx.core.widget.doAfterTextChanged
import androidx.lifecycle.Observer
import com.haraev.authentication.R
import com.haraev.authentication.di.component.LoginComponent
import com.haraev.core.ui.BaseFragment
import kotlinx.android.synthetic.main.fragment_login.*
import javax.inject.Inject
import javax.inject.Provider

class LoginFragment : BaseFragment(R.layout.fragment_login) {

    @Inject
    lateinit var viewModelFactory: Provider<LoginViewModel>

    private val viewModel by lazy { viewModelFactory.get() }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        LoginComponent.Builder.build().inject(this)

        observeViewModel()
        initView()
    }

    private fun observeViewModel() {
        viewModel.uiState.observe(viewLifecycleOwner, Observer {
            setEnterButtonEnabled(it.enableLoginButton)
        })
    }

    private fun initView() {
        setupLoginEdt()
        setupPasswordEdt()
        setupEnterBtn()
    }

    private fun setupEnterBtn() {
        btn_enter.setOnClickListener {
            viewModel.login(edt_login.text.toString(), edt_pass.text.toString())
            hideKeyboard()
        }
    }

    private fun setupLoginEdt() {
        edt_login.doAfterTextChanged {
            viewModel.loginDataChanged(it.toString(), edt_pass.text.toString())
        }
    }

    private fun setupPasswordEdt() {
        edt_pass.apply {
            doAfterTextChanged {
                viewModel.loginDataChanged(edt_login.text.toString(), it.toString())
            }
            setOnEditorActionListener { _, actionId, _ ->
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    viewModel.login(edt_login.text.toString(), edt_pass.text.toString())
                    hideKeyboard()
                }
                false
            }
        }
    }

    private fun setEnterButtonEnabled(boolean: Boolean) {
        btn_enter.isEnabled = boolean
    }
}