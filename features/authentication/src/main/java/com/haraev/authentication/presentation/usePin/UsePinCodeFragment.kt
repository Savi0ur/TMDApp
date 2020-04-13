package com.haraev.authentication.presentation.usePin

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import com.haraev.authentication.R
import com.haraev.authentication.di.component.UsePinCodeComponent
import com.haraev.authentication.presentation.item.KeyboardDummyItem
import com.haraev.authentication.presentation.item.KeyboardExitItem
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
import kotlinx.android.synthetic.main.fragment_use_pin.*
import javax.inject.Inject

class UsePinCodeFragment : BaseFragment(R.layout.fragment_use_pin){

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val viewModel: UsePinCodeViewModel by viewModels { viewModelFactory }

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
        setupKeyboardRecycler()
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
            is ShowErrorMessage -> showErrorMessage(event.messageResId)
        }
    }

    private fun renderState(viewState: UsePinCodeViewState) {
        use_pin_indicators.turnOffAllIndicators()
        viewState.pinCode.forEach { _ ->
            use_pin_indicators.turnOnNextIndicator()
        }

        use_pin_progress_bar.isVisible = viewState.showProgressBar
        use_pin_ui_blocker.isVisible = viewState.showProgressBar

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
                is KeyboardExitItem -> {
                    viewModel.logout()
                }
            }
        }

        keyboardAdapter.setOnItemClickListener(onItemClickListener)

        with(use_pin_keyboard_recycler) {
            layoutManager = androidx.recyclerview.widget.GridLayoutManager(requireContext(), 3)
            adapter = keyboardAdapter
        }

        (1..9).forEach {
            keyboardAdapter.add(KeyboardTextItem(it.toString()))
        }

        keyboardAdapter.add(KeyboardExitItem())
        keyboardAdapter.add(KeyboardTextItem("0"))
        keyboardAdapter.add(KeyboardImageItem(R.drawable.drawable_backspace))

    }

    private fun navigateToLoginScreen() {
        (requireActivity() as NavigationActivity).navigateToLoginScreen()
    }

    private fun navigateNext() {
        (requireActivity() as NavigationActivity).navigateToMainScreen()
    }
}