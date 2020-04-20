package com.haraev.main.presentation.favorite

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
import com.haraev.core.aac.observe
import com.haraev.core.di.provider.CoreComponentProvider
import com.haraev.core.ui.BaseFragment
import com.haraev.core.ui.Event
import com.haraev.core.ui.ShowErrorMessage
import com.haraev.main.R
import com.haraev.main.data.model.response.MovieDetailsResponse
import com.haraev.main.di.component.FavoriteComponent
import com.haraev.main.presentation.item.MovieGridItem
import com.haraev.main.presentation.item.MovieItem
import com.haraev.main.presentation.item.MovieLinearItem
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.OnItemClickListener
import com.xwray.groupie.kotlinandroidextensions.GroupieViewHolder
import kotlinx.android.synthetic.main.favorite_toolbar.*
import kotlinx.android.synthetic.main.fragment_favorite.*
import javax.inject.Inject

class FavoriteFragment : BaseFragment(R.layout.fragment_favorite) {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val viewModel: FavoriteViewModel by viewModels { viewModelFactory }

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
        FavoriteComponent.Builder
            .build((requireActivity().application as CoreComponentProvider).getCoreComponent())
            .inject(this)

        super.onAttach(context)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        observeViewModels()
        initViews()
    }

    private fun observeViewModels() {
        observe(viewModel.uiState, ::renderState)
        observe(viewModel.eventsQueue, ::onEvent)
    }

    private fun renderState(viewState: FavoriteViewState) {
        favorite_progress_bar.isVisible = viewState.progressBarVisibility
        if (!viewState.progressBarVisibility) {
            favorite_swipe_refresh_layout.isRefreshing = false
        }

        if (viewState.movies.isEmpty()) {
            showMovies(viewState.movies)
            showDefaultScreen()
        } else {
            if (viewState.searchQuery.isNotEmpty()) {
                val filtredMovies = viewState.movies.filter { it.title.contains(viewState.searchQuery, true) }
                if (filtredMovies.isNotEmpty()) {
                    showMovies(filtredMovies)
                } else {
                    showNoMoviesFoundByQuery()
                }
            } else {
                showMovies(viewState.movies)
            }
        }

        if (viewState.searchViewVisibility) {
            favorite_motion_layout.transitionToEnd()
        } else {
            favorite_motion_layout.transitionToStart()
            hideKeyboard()
        }
    }

    private fun onEvent(event: Event) {
        when (event) {
            is ShowErrorMessage -> showErrorMessage(
                event.messageResId,
                R.id.favorite_root_coordinator
            )
        }
    }

    private fun initViews() {
        setupMenu()
        setupMoviesRecycler()
        setupSearchNewMoviesTextView()
        setupCloseImage()
        setupSearchView()
        setupSwipeRefreshLayout()
    }

    private fun setupSwipeRefreshLayout() {
        favorite_swipe_refresh_layout.setOnRefreshListener {
            viewModel.loadFavoriteMovies()
        }
    }

    private fun setupSearchView() {
        favorite_search_view.setOnSearchClickListener {
            hideKeyboard()
        }

        favorite_search_view.setOnClearClickListener {
            favorite_search_view.clear()
        }

        viewModel.onSearchEditTextTextChanged(
            favorite_search_view.textChanges()
                .map { it.toString() }
        )
    }

    private fun setupMoviesRecycler() {
        moviesAdapter.setOnItemClickListener(moviesItemClickListener)
        with(favorite_recycler) {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = moviesAdapter
        }
    }

    private fun setupCloseImage() {
        favorite_close_image.setOnClickListener {
            viewModel.onCloseImageClicked()
        }
    }

    private fun setupMenu() {
        favorite_toolbar.setOnMenuItemClickListener { item ->
            when (item.itemId) {
                R.id.favorite_list_type -> {
                    val isChecked = !item.isChecked

                    if (isChecked) {
                        item.setIcon(R.drawable.drawable_linear_check_box)
                    } else {
                        item.setIcon(R.drawable.drawable_grid_check_box)
                    }

                    changeRecyclerViewLayoutManager(isChecked)
                    showMovies(viewModel.uiState.value?.movies ?: emptyList())

                    item.isChecked = isChecked
                    true
                }
                R.id.favorite_search_icon -> {
                    viewModel.onSearchIconClicked()
                    true
                }
                else -> false
            }
        }
    }

    private fun setupSearchNewMoviesTextView() {
        favorite_link_text.setOnClickListener {
            viewModel.onSearchNewMoviesTextClicked()
        }
    }

    private fun changeRecyclerViewLayoutManager(isGrid: Boolean) {
        favorite_recycler.layoutManager = if (isGrid) {
            val spanCount = 2
            GridLayoutManager(requireContext(), spanCount)
        } else {
            LinearLayoutManager(requireContext())
        }
    }

    private fun showMovies(movies: List<MovieDetailsResponse>) {
        moviesAdapter.update(
            if (favorite_recycler.layoutManager is GridLayoutManager) {
                movies.map {
                    MovieGridItem(it)
                }
            } else {
                movies.map {
                    MovieLinearItem(it)
                }
            }
        )

        favorite_no_movies_added_image.isInvisible = true
        favorite_no_movies_text.isInvisible = true
        favorite_link_text.isInvisible = true
        favorite_recycler.isVisible = true

        favorite_no_movies_found_image.isVisible = false
        favorite_no_movies_found_text.isVisible = false
    }

    private fun showDefaultScreen() {
        favorite_no_movies_added_image.isVisible = true
        favorite_recycler.isInvisible = true
        favorite_no_movies_text.isVisible = true
        favorite_link_text.isVisible = true
    }

    private fun showNoMoviesFoundByQuery() {
        favorite_no_movies_added_image.isVisible = false
        favorite_no_movies_text.isVisible = false
        favorite_link_text.isVisible = false
        favorite_recycler.isVisible = false

        favorite_no_movies_found_image.isVisible = true
        favorite_no_movies_found_text.isVisible = true
    }

    private fun navigateToMovieDetailsScreen(
        movie: MovieDetailsResponse,
        extras: FragmentNavigator.Extras
    ) {

        val direction = FavoriteFragmentDirections.actionFavoriteFragmentToMovieDetailsFragment2(
            movieTitle = movie.title,
            moviePosterPath = movie.posterPath,
            movieOverview = movie.overview,
            movieReleaseDate = movie.releaseDate,
            movieOriginalTitle = movie.originalTitle,
            movieVoteCount = movie.voteCount,
            movieVoteAverage = movie.voteAverage.toFloat(),
            movieGenres = movie.genres.joinToString(separator = ", ") { it.name },
            movieDuration = movie.duration?.toString() ?: "0",
            movieServerId = movie.serverId
        )

        findNavController().navigate(direction, extras)
    }

}