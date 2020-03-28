package com.haraev.tmdapp.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.lifecycle.observe
import androidx.navigation.Navigation
import com.haraev.core.di.provider.CoreComponentProvider
import com.haraev.tmdapp.R
import com.haraev.tmdapp.di.component.MainComponent
import javax.inject.Inject

class MainActivity : AppCompatActivity() {

    @Inject
    lateinit var viewModelFactory: MainViewModelFactory

    private val viewModel: MainViewModel by viewModels { viewModelFactory }

    override fun onCreate(savedInstanceState: Bundle?) {
        MainComponent.Builder.build((application as CoreComponentProvider).getCoreComponent())
            .inject(this)

        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)

        observeViewModel()
    }

    private fun observeViewModel() {
        viewModel.uiCommand.observe(this, ::handleViewCommand)
    }

    private fun handleViewCommand(viewCommand: MainViewCommand) {
        when (viewCommand) {
            is MainViewCommand.OpenLoginScreen -> {
                Navigation.findNavController(this, R.id.nav_host_fragment)
                    .setGraph(R.navigation.login_nav_graph)
            }
            is MainViewCommand.OpenSearchScreen -> {
                /**
                 * Заглушка
                 * TODO Переход на основной экран при наличии sessionId
                 */
            }
        }
    }

    override fun onSupportNavigateUp() =
        Navigation.findNavController(this, R.id.nav_host_fragment).navigateUp()
}
