package com.haraev.tmdapp.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import com.haraev.core.ui.Event
import com.haraev.core.aac.observe
import com.haraev.core.di.provider.CoreComponentProvider
import com.haraev.core.navigation.NavigationActivity
import com.haraev.tmdapp.R
import com.haraev.tmdapp.di.component.MainComponent
import javax.inject.Inject

class MainActivity : AppCompatActivity(), NavigationActivity {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val viewModel: MainViewModel by viewModels { viewModelFactory }

    override fun onCreate(savedInstanceState: Bundle?) {
        MainComponent.Builder.build((application as CoreComponentProvider).getCoreComponent())
            .inject(this)

        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)

        observeViewModel()
    }

    override fun navigateToLoginScreen() {
        Navigation.findNavController(this, R.id.nav_host_fragment)
            .setGraph(R.navigation.login_nav_graph)
    }

    override fun navigateToMainScreen() {
        Navigation.findNavController(this, R.id.nav_host_fragment)
            .setGraph(R.navigation.main_graph)
    }

    override fun navigateToUsePinCodeScreen() {
        Navigation.findNavController(this, R.id.nav_host_fragment)
            .setGraph(R.navigation.use_pin_code_nav_graph)
    }

    private fun observeViewModel() {
        observe(viewModel.eventsQueue, ::onEvent)
    }

    private fun onEvent(event: Event) {
        when (event) {
            is MainEvents.OpenLoginScreen -> {
                navigateToLoginScreen()
            }
            is MainEvents.OpenSearchScreen -> {
                navigateToMainScreen()
            }
            is MainEvents.OpenUsePinCodeScreen -> {
                navigateToUsePinCodeScreen()
            }
        }
    }

    override fun onSupportNavigateUp() =
        Navigation.findNavController(this, R.id.nav_host_fragment).navigateUp()
}
