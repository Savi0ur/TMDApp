package com.haraev.main.presentation.profile

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import com.haraev.core.ui.Event
import com.haraev.core.aac.observe
import com.haraev.core.di.provider.CoreComponentProvider
import com.haraev.core.navigation.NavigationActivity
import com.haraev.core.ui.BaseFragment
import com.haraev.core.ui.ShowErrorMessage
import com.haraev.main.R
import com.haraev.main.di.component.ProfileComponent
import kotlinx.android.synthetic.main.fragment_profile.*
import javax.inject.Inject

class ProfileFragment : BaseFragment(R.layout.fragment_profile) {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val viewModel: ProfileViewModel by viewModels { viewModelFactory }

    override fun onAttach(context: Context) {
        ProfileComponent.Builder
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
        setupLogoutButton()
    }

    private fun setupLogoutButton() {
        profile_logout_button.setOnClickListener { viewModel.exitButtonClicked() }
    }

    private fun observeViewModel() {
        observe(viewModel.uiState, ::renderState)
        observe(viewModel.eventsQueue, ::onEvent)
    }

    private fun onEvent(event: Event) {
        when (event) {
            is ProfileEvents.Logout -> (requireActivity() as NavigationActivity).navigateToLoginScreen()
            is ShowErrorMessage -> showErrorMessage(event.messageResId, R.id.profile_root_coordinator, profile_logout_button)
        }
    }

    private fun renderState(viewState: ProfileViewState) {
        when (viewState.progressBarVisibility) {
            true -> profile_progress_bar.visibility = View.VISIBLE
            false -> profile_progress_bar.visibility = View.INVISIBLE
        }

        viewState.name?.let {
            profile_name_text_view.text = it
        }

        viewState.userName?.let {
            profile_username_text_view.text = it
        }
    }
}