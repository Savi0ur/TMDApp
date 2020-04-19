package com.haraev.authentication.presentation.pinCode.usePin

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import com.haraev.authentication.R
import com.haraev.authentication.di.component.UsePinCodeComponent
import com.haraev.authentication.presentation.pinCode.BasePinCodeFragment
import com.haraev.core.aac.observe
import com.haraev.core.di.provider.CoreComponentProvider
import com.haraev.core.navigation.NavigationActivity
import com.haraev.core.ui.Event
import com.haraev.core.ui.ShowErrorMessage
import kotlinx.android.synthetic.main.fragment_use_pin.*
import javax.inject.Inject

class UsePinCodeFragment : BasePinCodeFragment(R.layout.fragment_use_pin) {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    override val viewModel: UsePinCodeViewModel by viewModels { viewModelFactory }

    override fun onAttach(context: Context) {
        UsePinCodeComponent.Builder
            .build((requireActivity().application as CoreComponentProvider).getCoreComponent())
            .inject(this)

        super.onAttach(context)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        observeViewModel()
        initView()
    }

    private fun initView() {
        setupKeyboardRecyclerWithExitButton(use_pin_keyboard_recycler)
    }

    private fun observeViewModel() {
        observe(viewModel.uiState, ::renderState)
        observe(viewModel.eventsQueue, ::onEvent)
    }

    private fun onEvent(event: Event) {
        when (event) {
            is UsePinCodeEvents.NavigateToMainFeature -> navigateNext()
            is UsePinCodeEvents.NavigateToLoginScreen -> navigateToLoginScreen()
            is UsePinCodeEvents.WrongPin -> use_pin_error_text.setText(event.messageResId)
            is UsePinCodeEvents.TryUseFingerPrint -> tryUseFingerPrint()
            is ShowErrorMessage -> showErrorMessage(event.messageResId)
        }
    }

    private fun renderState(viewState: UsePinCodeViewState) {
        use_pin_indicators.turnOnNextIndicators(viewState.pinCode.length)

        use_pin_progress_bar.isVisible = viewState.showProgressBar
        use_pin_ui_blocker.isVisible = viewState.showProgressBar

        use_pin_header_text.text = viewState.userName
    }

    private fun navigateToLoginScreen() {
        (requireActivity() as NavigationActivity).navigateToLoginScreen()
    }

    private fun navigateNext() {
        (requireActivity() as NavigationActivity).navigateToMainScreen()
    }

    private fun tryUseFingerPrint() {

        if (isBiometricAvailable()) {
            showFingerprintDialog()
        } else {
            viewModel.onBiometricUnavailable()
        }
    }
}