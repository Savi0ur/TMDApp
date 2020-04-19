package com.haraev.authentication.presentation.pinCode.repeatPin

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.haraev.authentication.R
import com.haraev.authentication.di.component.RepeatPinCodeComponent
import com.haraev.authentication.presentation.pinCode.BasePinCodeFragment
import com.haraev.core.aac.observe
import com.haraev.core.di.provider.CoreComponentProvider
import com.haraev.core.navigation.NavigationActivity
import com.haraev.core.ui.Event
import com.haraev.core.ui.ShowErrorMessage
import kotlinx.android.synthetic.main.fragment_repeat_pin.*
import javax.inject.Inject

class RepeatPinCodeFragment : BasePinCodeFragment(R.layout.fragment_repeat_pin) {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    override val viewModel: RepeatPinCodeViewModel by viewModels { viewModelFactory }

    private val args by navArgs<RepeatPinCodeFragmentArgs>()

    override fun onAttach(context: Context) {
        RepeatPinCodeComponent.Builder
            .build((requireActivity().application as CoreComponentProvider).getCoreComponent())
            .inject(this)

        super.onAttach(context)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        observeViewModel()
        initView()
        initArgsContent()
    }

    private fun initArgsContent() {
        viewModel.setPin(args.pinCode)
    }

    private fun initView() {
        setupToolbar()
        setupKeyboardRecycler(repeat_pin_keyboard_recycler)
    }

    private fun observeViewModel() {
        observe(viewModel.uiState, ::renderState)
        observe(viewModel.eventsQueue, ::onEvent)
    }

    private fun onEvent(event: Event) {
        when (event) {
            is RepeatPinCodeEvents.NavigateToMainFeature -> navigateNext()
            is RepeatPinCodeEvents.WrongPin -> repeat_pin_indicators.showError()
            is RepeatPinCodeEvents.TryUseFingerPrint -> tryFingerPrint()
            is ShowErrorMessage -> showErrorMessage(event.messageResId)
        }
    }

    private fun renderState(viewState: RepeatPinCodeViewState) {
        repeat_pin_indicators.turnOnNextIndicators(viewState.repeatPinCode.length)
    }

    private fun setupToolbar() {
        repeat_pin_toolbar.setNavigationOnClickListener {
            findNavController().navigateUp()
        }
    }

    private fun navigateNext() {
        (requireActivity() as NavigationActivity).navigateToMainScreen()
    }

    private fun tryFingerPrint() {
        if (isBiometricAvailable()) {
            showFingerPrintSuggestDialog()
        } else {
            viewModel.onBiometricUnavailable()
        }
    }

    private fun showFingerPrintSuggestDialog() {
        val dialog = AlertDialog
            .Builder(requireContext())
            .setMessage(R.string.suggest_fingerprint)
            .setPositiveButton(R.string.yes) { _, _ -> showFingerprintDialog() }
            .setNegativeButton(R.string.no) { _, _ -> viewModel.onBiometricUnavailable() }
            .create()
        dialog.setCanceledOnTouchOutside(false)
        dialog.show()
    }
}