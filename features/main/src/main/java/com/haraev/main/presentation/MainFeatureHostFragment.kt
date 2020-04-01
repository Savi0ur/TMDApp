package com.haraev.main.presentation

import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import com.haraev.core.ui.BaseFragment
import com.haraev.main.R
import kotlinx.android.synthetic.main.fragment_main.*

class MainFeatureHostFragment : BaseFragment(R.layout.fragment_main) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setBottomNavigation()
    }

    private fun setBottomNavigation() {
        val hostFragment = childFragmentManager
            .findFragmentById(R.id.main_fragment_nav_host_fragment) as? NavHostFragment

        hostFragment?.findNavController()?.let {
            bottom_navigation.setupWithNavController(it)
        }
    }
}