package com.haraev.authentication.presentation.repeatPin

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.biometric.BiometricConstants
import androidx.biometric.BiometricManager
import androidx.biometric.BiometricPrompt
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.GridLayoutManager
import com.haraev.authentication.R
import com.haraev.authentication.di.component.RepeatPinCodeComponent
import com.haraev.authentication.presentation.item.KeyboardDummyItem
import com.haraev.authentication.presentation.item.KeyboardImageItem
import com.haraev.authentication.presentation.item.KeyboardTextItem
import com.haraev.core.aac.observe
import com.haraev.core.di.provider.CoreComponentProvider
import com.haraev.core.navigation.NavigationActivity
import com.haraev.core.ui.BaseFragment
import com.haraev.core.ui.Event
import com.haraev.core.ui.ShowErrorMessage
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.OnItemClickListener
import com.xwray.groupie.kotlinandroidextensions.GroupieViewHolder
import kotlinx.android.synthetic.main.fragment_repeat_pin.*
import java.util.concurrent.Executors
import javax.inject.Inject

class RepeatPinCodeFragment : BaseFragment(R.layout.fragment_repeat_pin) {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val viewModel: RepeatPinCodeViewModel by viewModels { viewModelFactory }

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
        setupKeyboardRecycler()
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
        repeat_pin_indicators.turnOffAllIndicators()
        viewState.repeatPinCode.forEach { _ ->
            repeat_pin_indicators.turnOnNextIndicator()
        }
    }


    private fun setupToolbar() {
        repeat_pin_toolbar.setNavigationOnClickListener {
            findNavController().navigateUp()
        }
    }

    private fun setupKeyboardRecycler() {

        val keyboardAdapter = GroupAdapter<GroupieViewHolder>()

        val onItemClickListener = OnItemClickListener { item, _ ->
            when (item) {
                is KeyboardTextItem -> {
                    viewModel.onKeyboardTextItemClicked(item.text)
                }
                is KeyboardImageItem -> {
                    if (item.drawableId == R.drawable.drawable_backspace) {
                        viewModel.onKeyboardBackspaceClicked()
                    }
                }
            }
        }

        keyboardAdapter.setOnItemClickListener(onItemClickListener)

        with(repeat_pin_keyboard_recycler)
        {
            layoutManager = GridLayoutManager(requireContext(), 3)
            adapter = keyboardAdapter
        }

        (1..9).forEach {
            keyboardAdapter.add(KeyboardTextItem(it.toString()))
        }

        keyboardAdapter.add(KeyboardDummyItem())
        keyboardAdapter.add(KeyboardTextItem("0"))
        keyboardAdapter.add(KeyboardImageItem(R.drawable.drawable_backspace))

    }

    private fun navigateNext() {
        (requireActivity() as NavigationActivity).navigateToMainScreen()
    }

    private fun tryFingerPrint() {
        val biometricManager = BiometricManager.from(requireContext())

        if (biometricManager.canAuthenticate() == BiometricManager.BIOMETRIC_SUCCESS) {

            val biometricExecutor = Executors.newSingleThreadExecutor()

            val biometricPrompt = BiometricPrompt(
                this,
                biometricExecutor,
                object : BiometricPrompt.AuthenticationCallback() {
                    override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
                        super.onAuthenticationError(errorCode, errString)

                        if (errorCode == BiometricConstants.ERROR_CANCELED || errorCode == BiometricConstants.ERROR_USER_CANCELED) {
                            requireActivity().runOnUiThread { viewModel.biometricCancelled() }
                        } else {
                            requireActivity().runOnUiThread { viewModel.biometricFailed() }
                        }
                    }

                    override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                        super.onAuthenticationSucceeded(result)

                        requireActivity().runOnUiThread { viewModel.biometricSucceeded() }
                    }

                    override fun onAuthenticationFailed() {
                        super.onAuthenticationFailed()

                        requireActivity().runOnUiThread { viewModel.biometricFailed() }
                    }
                }
            )

            val promptInfo = biometricPromptInfo()
            biometricPrompt.authenticate(promptInfo)

        } else {
            viewModel.biometricUnavailable()
        }
    }

    private fun biometricPromptInfo(): BiometricPrompt.PromptInfo {
        return BiometricPrompt.PromptInfo.Builder()
            .setTitle("Отпечаток пальца")
            .setDescription("Подтвердите отпечаток пальца, чтобы пользоваться быстрой авторизацией")
            .setNegativeButtonText(requireActivity().getString(android.R.string.cancel))
            .build()
    }
}