package com.haraev.main.presentation.search

import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.view.isInvisible
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.FragmentNavigator
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.haraev.core.aac.Event
import com.haraev.core.aac.observe
import com.haraev.core.di.provider.CoreComponentProvider
import com.haraev.core.ui.BaseFragment
import com.haraev.main.R
import com.haraev.main.data.model.Movie
import com.haraev.main.di.component.MainFeatureComponent
import com.haraev.main.presentation.adapter.MoviesAdapter
import com.jakewharton.rxbinding3.widget.textChanges
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_main.*
import kotlinx.android.synthetic.main.fragment_search.*
import kotlinx.android.synthetic.main.item_movie_grid.*
import javax.inject.Inject

class SearchFragment : BaseFragment(R.layout.fragment_search) {

    @Inject
    lateinit var viewModelFactory: SearchViewModelFactory

    private val viewModel: SearchViewModel by viewModels { viewModelFactory }

    private val moviesAdapter: MoviesAdapter = MoviesAdapter()

    private val moviesItemClickListener = object : MoviesAdapter.ItemClickListener {
        override fun onItemClick(movie: Movie, extras: FragmentNavigator.Extras) {
            navigateToMovieDetailsScreen(movie, extras)
        }
    }

    override fun onAttach(context: Context) {
        MainFeatureComponent.Builder
            .build((requireActivity().application as CoreComponentProvider).getCoreComponent())
            .inject(this)

        super.onAttach(context)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        observeViewModel()
        initViews()
    }

    private fun observeViewModel() {
        observe(viewModel.uiState, ::renderState)
        observe(viewModel.eventsQueue, ::onEvent)
    }

    private fun onEvent(event: Event) {
        when (event) {
            is SearchEvents.ErrorMessage -> showErrorMessage(event.messageResId, bottom_navigation)
        }
    }

    private fun renderState(viewState: SearchViewState) {
        showMovies(viewState.movies ?: emptyList())
        viewState.movies?.let {
            if (it.isEmpty()) {
                showNoMovies()
            }
        } ?: showDefaultScreen()
    }

    private fun initViews() {
        setupMoviesRecycler()
        setupSearchEditText()
        setupClearButton()
    }

    private fun setupClearButton() {
        search_clear_button.setOnClickListener {
            search_search_edit_text.text = null
        }
    }

    private fun setupSearchEditText() {
        viewModel.onSearchEditTextTextChanged(
            search_search_edit_text.textChanges()
                .map { it.toString() }
        )
    }

    private fun setupMoviesRecycler() {
        with(search_recycler_view) {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = moviesAdapter
        }

        moviesAdapter.setOnItemClickListener(moviesItemClickListener)
    }

    private fun showDefaultScreen() {
        search_default_image.isVisible = true
        search_recycler_view.isInvisible = true
        search_no_movies_image.isInvisible = true
        search_no_movies_text.isInvisible = true
    }

    private fun showNoMovies() {
        search_default_image.isInvisible = true
        search_recycler_view.isInvisible = true
        search_no_movies_image.isVisible = true
        search_no_movies_text.isVisible = true
    }

    private fun showMovies(movies: List<Movie>) {
        moviesAdapter.updateMovies(movies)

        search_default_image.isInvisible = true
        search_recycler_view.isVisible = true
        search_no_movies_image.isInvisible = true
        search_no_movies_text.isInvisible = true
    }

    private fun navigateToMovieDetailsScreen(movie: Movie, extras: FragmentNavigator.Extras) {

        val direction = SearchFragmentDirections.actionSearchFragmentToMovieDetailsFragment(
            movieTitle = movie.title
        )

        findNavController().navigate(direction, extras)
    }

}