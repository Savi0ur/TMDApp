package com.haraev.authentication.presentation.pinCode

import androidx.biometric.BiometricConstants
import androidx.biometric.BiometricManager
import androidx.biometric.BiometricPrompt
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.haraev.authentication.R
import com.haraev.authentication.presentation.common.MainThreadExecutor
import com.haraev.authentication.presentation.item.KeyboardDummyItem
import com.haraev.authentication.presentation.item.KeyboardExitItem
import com.haraev.authentication.presentation.item.KeyboardImageItem
import com.haraev.authentication.presentation.item.KeyboardTextItem
import com.haraev.core.ui.BaseFragment
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.OnItemClickListener
import com.xwray.groupie.kotlinandroidextensions.GroupieViewHolder

abstract class BasePinCodeFragment(fragmentLayoutId: Int) : BaseFragment(fragmentLayoutId) {

    private val keyboardAdapter = GroupAdapter<GroupieViewHolder>()

    abstract val viewModel: PinCodeViewModel

    fun setupKeyboardRecycler(
        recyclerView: RecyclerView
    ) {
        defaultKeyboardInit(recyclerView)

        keyboardAdapter.add(KeyboardDummyItem())
        keyboardAdapter.add(KeyboardTextItem("0"))
        keyboardAdapter.add(KeyboardImageItem(R.drawable.drawable_backspace))
    }

    fun setupKeyboardRecyclerWithExitButton(
        recyclerView: RecyclerView
    ) {
        defaultKeyboardInit(recyclerView)

        keyboardAdapter.add(KeyboardExitItem())
        keyboardAdapter.add(KeyboardTextItem("0"))
        keyboardAdapter.add(KeyboardImageItem(R.drawable.drawable_backspace))

    }

    fun showFingerprintDialog() {
        val biometricExecutor = MainThreadExecutor()

        val biometricPrompt = BiometricPrompt(
            this,
            biometricExecutor,
            object : BiometricPrompt.AuthenticationCallback() {
                override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
                    super.onAuthenticationError(errorCode, errString)

                    if (errorCode == BiometricConstants.ERROR_CANCELED || errorCode == BiometricConstants.ERROR_USER_CANCELED) {
                        viewModel.onBiometricCancelled()
                    } else {
                        viewModel.onBiometricFailed()
                    }
                }

                override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                    super.onAuthenticationSucceeded(result)

                    viewModel.onBiometricSucceed()
                }
            }
        )

        val promptInfo = biometricPromptInfo()
        biometricPrompt.authenticate(promptInfo)
    }

    fun isBiometricAvailable(): Boolean {
        val biometricManager = BiometricManager.from(requireContext())
        return biometricManager.canAuthenticate() == BiometricManager.BIOMETRIC_SUCCESS
    }

    private fun defaultKeyboardInit(
        recyclerView: RecyclerView
    ) {
        val onItemClickListener = OnItemClickListener { item, _ ->
            when (item) {
                is KeyboardTextItem -> {
                    viewModel.onKeyboardTextItemClicked(item.text)
                }
                is KeyboardImageItem -> {
                    if (item.drawableId == R.drawable.drawable_backspace) {
                        viewModel.onKeyboardBackspaceItemClicked()
                    }
                }
                is KeyboardExitItem -> {
                    viewModel.onKeyboardExitItemClicked()
                }
            }
        }

        keyboardAdapter.setOnItemClickListener(onItemClickListener)

        with(recyclerView) {
            layoutManager = GridLayoutManager(requireContext(), 3)
            adapter = keyboardAdapter
        }

        (1..9).forEach {
            keyboardAdapter.add(KeyboardTextItem(it.toString()))
        }
    }

    private fun biometricPromptInfo(): BiometricPrompt.PromptInfo {
        return BiometricPrompt.PromptInfo.Builder()
            .setTitle(getString(R.string.fingerprint_title))
            .setDescription(getString(R.string.finerprint_description))
            .setNegativeButtonText(getString(android.R.string.cancel))
            .build()
    }
}