package com.haraev.main.presentation.moviedetails

import android.content.Context
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.transition.TransitionInflater
import com.haraev.core.aac.observe
import com.haraev.core.common.BASE_IMAGE_URL
import com.haraev.core.di.provider.CoreComponentProvider
import com.haraev.core.ui.BaseFragment
import com.haraev.core.ui.Event
import com.haraev.core.ui.ShowErrorMessage
import com.haraev.main.R
import com.haraev.main.di.component.MovieDetailsComponent
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_movie_details.*
import kotlinx.android.synthetic.main.movie_details_toolbar.*
import javax.inject.Inject

class MovieDetailsFragment : BaseFragment(R.layout.fragment_movie_details) {

    private lateinit var addToFavoriteMenuItem: MenuItem

    private val args by navArgs<MovieDetailsFragmentArgs>()

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val viewModel: MovieDetailsViewModel by viewModels { viewModelFactory }

    override fun onAttach(context: Context) {
        MovieDetailsComponent.Builder
            .build((requireActivity().application as CoreComponentProvider).getCoreComponent())
            .inject(this)

        super.onAttach(context)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        sharedElementEnterTransition = TransitionInflater.from(requireContext())
            .inflateTransition(android.R.transition.move)

        observeViewModels()
        initViews()

        viewModel.initMovieId(args.movieServerId)
    }

    private fun initViews() {
        setupMenu()
        initContentFromArgs()
    }

    private fun observeViewModels() {
        observe(viewModel.uiState, ::renderState)
        observe(viewModel.eventsQueue, ::onEvent)
    }

    private fun renderState(viewState: MovieDetailsViewState) {
        movie_details_progress_bar.isVisible = viewState.progressBarVisibility
        addToFavoriteMenuItem.isEnabled =
            !(viewState.progressBarVisibility || viewState.markAsFavoriteInProcess)

        changeAddToFavoriteIconCheckedState(viewState.isFavoriteMovie)

    }

    private fun onEvent(event: Event) {
        when (event) {
            is ShowErrorMessage -> showErrorMessage(
                event.messageResId,
                R.id.movie_details_root_coordinator
            )
        }
    }

    private fun setupMenu() {
        addToFavoriteMenuItem =
            movie_details_toolbar.menu.findItem(R.id.movie_details_add_to_favorite)

        movie_details_toolbar.setNavigationOnClickListener {
            findNavController().navigateUp()
        }

        movie_details_toolbar.setOnMenuItemClickListener { item ->
            when (item.itemId) {
                R.id.movie_details_add_to_favorite -> {
                    val isChecked = !item.isChecked

                    viewModel.markAsFavorite(isChecked)

                    changeAddToFavoriteIconCheckedState(isChecked)
                    true
                }
                else -> false
            }
        }
    }

    private fun initContentFromArgs() {
        with(args) {

            setupMovieImage(moviePosterPath)

            movie_details_title_text_view.text = movieTitle

            val originalTitleWithYear = resources.getString(
                R.string.original_title_with_date,
                movieOriginalTitle,
                movieReleaseDate
            )
            movie_details_original_title_with_year_text.text = originalTitleWithYear

            movie_details_genres_text.text = movieGenres ?: ""

            movie_details_duration_content_text.text = movieDuration ?: "0"

            movie_details_rating_content_vote_average_text.text = movieVoteAverage.toString()
            movie_details_rating_content_vote_count_text.text = movieVoteCount.toString()

            movie_details_overview.text = movieOverview
        }
    }

    private fun setupMovieImage(moviePosterPath: String?) {
        moviePosterPath?.let {
            Picasso.get()
                .load("$BASE_IMAGE_URL$it")
                .placeholder(R.drawable.drawable_search)
                .into(movie_details_image)
        } ?: Picasso.get()
            .load(R.drawable.drawable_search)
            .placeholder(R.drawable.drawable_search)
            .into(movie_details_image)
    }

    private fun changeAddToFavoriteIconCheckedState(isChecked: Boolean) {
        if (isChecked) {
            addToFavoriteMenuItem.setIcon(R.drawable.drawable_action_favorite_filled)
        } else {
            addToFavoriteMenuItem.setIcon(R.drawable.drawable_action_favorite_border_outlined)
        }

        addToFavoriteMenuItem.isChecked = isChecked
    }
}