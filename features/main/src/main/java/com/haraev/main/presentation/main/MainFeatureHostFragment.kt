package com.haraev.main.presentation.main

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import com.haraev.core.aac.observe
import com.haraev.core.di.provider.CoreComponentProvider
import com.haraev.core.navigation.setupWithNavController
import com.haraev.core.ui.BaseFragment
import com.haraev.core.ui.Event
import com.haraev.core.ui.ShowErrorMessage
import com.haraev.main.R
import com.haraev.main.di.component.MainFeatureComponent
import kotlinx.android.synthetic.main.fragment_main.*
import javax.inject.Inject

class MainFeatureHostFragment : BaseFragment(R.layout.fragment_main) {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val viewModel: MainFeatureViewModel by viewModels { viewModelFactory }

    override fun onAttach(context: Context) {
        MainFeatureComponent.Builder
            .build((requireActivity().application as CoreComponentProvider).getCoreComponent())
            .inject(this)

        super.onAttach(context)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (savedInstanceState == null) {
            setBottomNavigation()
        }

        observeViewModel()
    }

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)

        setBottomNavigation()
    }

    private fun observeViewModel() {
        observe(viewModel.eventsQueue, ::onEvent)
    }
    private fun onEvent(event: Event) {
        when (event) {
            is ShowErrorMessage -> showErrorMessage(event.messageResId)
            is MainFeatureEvents.NavigateToSearchScreen -> bottom_navigation.selectedItemId =
                R.id.search_graph
            is MainFeatureEvents.NavigateToFavoriteScreen -> bottom_navigation.selectedItemId =
                R.id.favorite_graph
            is MainFeatureEvents.NavigateToProfileScreen -> bottom_navigation.selectedItemId =
                R.id.profile_graph
        }
    }

    private fun setBottomNavigation() {
        val navGraphIds = listOf(
            R.navigation.search_graph,
            R.navigation.favorite_graph,
            R.navigation.profile_graph
        )

        bottom_navigation.setupWithNavController(
            navGraphIds = navGraphIds,
            fragmentManager = childFragmentManager,
            containerId = R.id.main_fragment_nav_host_fragment,
            intent = requireActivity().intent
        )
    }
}
