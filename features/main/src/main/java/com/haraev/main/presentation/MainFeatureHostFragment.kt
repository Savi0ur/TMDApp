package com.haraev.main.presentation

import android.os.Bundle
import android.view.View
import com.haraev.core.navigation.setupWithNavController
import com.haraev.core.ui.BaseFragment
import com.haraev.main.R
import kotlinx.android.synthetic.main.fragment_main.*

class MainFeatureHostFragment : BaseFragment(R.layout.fragment_main) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (savedInstanceState == null) {
            setBottomNavigation()
        }
    }

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)

        setBottomNavigation()
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
