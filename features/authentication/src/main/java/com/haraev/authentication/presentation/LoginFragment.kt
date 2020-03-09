package com.haraev.authentication.presentation

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.haraev.authentication.R
import com.haraev.authentication.di.component.LoginComponent
import kotlinx.android.synthetic.main.fragment_login.*
import javax.inject.Inject
import javax.inject.Provider

class LoginFragment : Fragment() {

    @Inject
    lateinit var viewModelFactory: Provider<LoginViewModel>

    private val viewModel by lazy { viewModelFactory.get() }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_login, container, false)
    }

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

    private fun hideKeyboard() {
        val imm = activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
        imm?.hideSoftInputFromWindow(view?.windowToken, 0)
    }
}