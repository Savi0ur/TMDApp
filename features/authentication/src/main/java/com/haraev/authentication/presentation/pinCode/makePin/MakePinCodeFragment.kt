package com.haraev.authentication.presentation.pinCode.makePin

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.haraev.authentication.R
import com.haraev.authentication.di.component.MakePinCodeComponent
import com.haraev.authentication.presentation.pinCode.BasePinCodeFragment
import com.haraev.core.aac.observe
import com.haraev.core.di.provider.CoreComponentProvider
import com.haraev.core.ui.Event
import com.haraev.core.ui.ShowErrorMessage
import kotlinx.android.synthetic.main.fragment_make_pin.*
import javax.inject.Inject

class MakePinCodeFragment : BasePinCodeFragment(R.layout.fragment_make_pin) {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    override val viewModel: MakePinCodeViewModel by viewModels { viewModelFactory }

    override fun onAttach(context: Context) {
        MakePinCodeComponent.Builder
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
        setupKeyboardRecycler(make_pin_keyboard_recycler)
    }

    private fun observeViewModel() {
        observe(viewModel.uiState, ::renderState)
        observe(viewModel.eventsQueue, ::onEvent)
    }

    private fun onEvent(event: Event) {
        when (event) {
            is MakePinCodeEvents.NavigateToRepeatPinCodeScreen -> navigateNext(event.pinCode)
            is ShowErrorMessage -> showErrorMessage(event.messageResId)
        }
    }

    private fun renderState(viewState: MakePinCodeViewState) {
        make_pin_indicators.turnOnNextIndicators(viewState.pinCode.length)
    }

    private fun navigateNext(pinCode: String) {
        val direction =
            MakePinCodeFragmentDirections.actionMakePinFragmentToRepeatPinFragment(
                pinCode = pinCode
            )

        findNavController().navigate(direction)
    }
}