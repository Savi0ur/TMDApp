package com.haraev.main.presentation.search

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.core.view.isInvisible
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.FragmentNavigator
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.haraev.core.ui.Event
import com.haraev.core.aac.observe
import com.haraev.core.di.provider.CoreComponentProvider
import com.haraev.core.ui.BaseFragment
import com.haraev.core.ui.ShowErrorMessage
import com.haraev.main.R
import com.haraev.main.data.model.response.MovieDetailsResponse
import com.haraev.main.di.component.SearchComponent
import com.haraev.main.presentation.item.MovieGridItem
import com.haraev.main.presentation.item.MovieItem
import com.haraev.main.presentation.item.MovieLinearItem
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.OnItemClickListener
import com.xwray.groupie.kotlinandroidextensions.GroupieViewHolder
import io.reactivex.Observable
import kotlinx.android.synthetic.main.fragment_search.*
import javax.inject.Inject

class SearchFragment : BaseFragment(R.layout.fragment_search) {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val viewModel: SearchViewModel by viewModels { viewModelFactory }

    private val moviesAdapter = GroupAdapter<GroupieViewHolder>()

    private val moviesItemClickListener = OnItemClickListener { item, _ ->
        if (item is MovieItem) {
            navigateToMovieDetailsScreen(
                item.movie,
                FragmentNavigatorExtras(
                    item.movieTitleView to getString(R.string.transition_movie_title),
                    item.movieImageView to getString(R.string.transition_movie_image),
                    item.movieOriginalTitleView to getString(R.string.transition_movie_original_title_with_year),
                    item.movieGenresView to getString(R.string.transition_movie_genres),
                    item.movieDurationView to getString(R.string.transition_movie_duration),
                    item.movieDurationDescriptionView to getString(R.string.transition_movie_duration_description),
                    item.movieVoteAverageView to getString(R.string.transition_movie_vote_average),
                    item.movieVoteCountView to getString(R.string.transition_movie_vote_count)
                )
            )
        }
    }

    override fun onAttach(context: Context) {
        SearchComponent.Builder
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
            is ShowErrorMessage -> showErrorMessage(
                event.messageResId,
                R.id.search_root_coordinator
            )
        }
    }

    private fun renderState(viewState: SearchViewState) {
        showMovies(viewState.movies ?: emptyList())
        viewState.movies?.let {
            if (it.isEmpty()) {
                showNoMovies()
            }
        } ?: showDefaultScreen()

        search_progress_bar.isVisible = viewState.progressBarVisibility
    }

    private fun initViews() {
        setupMoviesRecycler()
        setupRecyclerTypeCheckBox()
        setupSearchView()
    }

    private fun setupSearchView() {
        search_search_view.setOnSearchClickListener {
            viewModel.onSearchEditTextTextChanged(Observable.just(search_search_view.text))
            hideKeyboard()
        }

        search_search_view.setOnClearClickListener {
            search_search_view.clear()
            viewModel.onClearButtonClicked()
        }

        viewModel.onSearchEditTextTextChanged(
            search_search_view.textChanges()
                .map { it.toString() }
        )

        search_search_view.setOnEditorActionListener {
            viewModel.onSearchEditTextTextChanged(Observable.just(search_search_view.text))
            hideKeyboard()
        }
    }

    private fun setupRecyclerTypeCheckBox() {
        search_list_type_check_box.setOnCheckedChangeListener { _, isChecked ->
            changeRecyclerViewLayoutManager(isChecked)

            viewModel.uiState.value?.movies?.let {
                if (it.isNotEmpty()) {
                    showMovies(it)
                }
            }
        }
    }

    private fun setupMoviesRecycler() {
        moviesAdapter.setOnItemClickListener(moviesItemClickListener)
        with(search_recycler_view) {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = moviesAdapter
        }
    }

    private fun changeRecyclerViewLayoutManager(isGrid: Boolean) {
        search_recycler_view.layoutManager = if (isGrid) {
            val spanCount = 2
            GridLayoutManager(requireContext(), spanCount)
        } else {
            LinearLayoutManager(requireContext())
        }
    }

    private fun showMovies(movies: List<MovieDetailsResponse>) {
        val movieItems = mutableListOf<MovieItem>()

        movieItems.addAll(movies.map {

            if (search_list_type_check_box.isChecked) {
                MovieGridItem(it)
            } else {
                MovieLinearItem(it)
            }

        })

        moviesAdapter.update(movieItems)

        search_default_image.isInvisible = true
        search_recycler_view.isVisible = true
        search_no_movies_image.isInvisible = true
        search_no_movies_text.isInvisible = true
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

    private fun navigateToMovieDetailsScreen(
        movie: MovieDetailsResponse,
        extras: FragmentNavigator.Extras
    ) {

        val direction = SearchFragmentDirections.actionSearchFragmentToMovieDetailsFragment(
            movieTitle = movie.title,
            moviePosterPath = movie.posterPath,
            movieOverview = movie.overview,
            movieReleaseDate = movie.releaseDate,
            movieOriginalTitle = movie.originalTitle,
            movieVoteCount = movie.voteCount,
            movieVoteAverage = movie.voteAverage.toFloat(),
            movieGenres = movie.genres.joinToString(separator = ", ") { it.name },
            movieDuration = movie.duration?.toString() ?: "0"
        )

        findNavController().navigate(direction, extras)
    }

}