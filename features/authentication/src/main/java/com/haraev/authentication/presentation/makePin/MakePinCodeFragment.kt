package com.haraev.authentication.presentation.makePin

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.haraev.authentication.R
import com.haraev.authentication.di.component.MakePinCodeComponent
import com.haraev.authentication.presentation.item.KeyboardDummyItem
import com.haraev.authentication.presentation.item.KeyboardImageItem
import com.haraev.authentication.presentation.item.KeyboardTextItem
import com.haraev.core.aac.observe
import com.haraev.core.di.provider.CoreComponentProvider
import com.haraev.core.ui.BaseFragment
import com.haraev.core.ui.Event
import com.haraev.core.ui.ShowErrorMessage
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.OnItemClickListener
import com.xwray.groupie.kotlinandroidextensions.GroupieViewHolder
import kotlinx.android.synthetic.main.fragment_make_pin.*
import javax.inject.Inject

class MakePinCodeFragment : BaseFragment(R.layout.fragment_make_pin) {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val viewModel: MakePinCodeViewModel by viewModels { viewModelFactory }

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
        setupKeyboardRecycler()
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
        make_pin_indicators.turnOffAllIndicators()
        viewState.pinCode.forEach { _ ->
            make_pin_indicators.turnOnNextIndicator()
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

        with(make_pin_keyboard_recycler) {
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

    private fun navigateNext(pinCode: String) {
        val direction =
            MakePinCodeFragmentDirections.actionMakePinFragmentToRepeatPinFragment(
                pinCode = pinCode
            )

        findNavController().navigate(direction)
    }
}