package com.haraev.main.presentation.moviedetails

import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.transition.TransitionInflater
import com.haraev.core.common.BASE_IMAGE_URL
import com.haraev.core.ui.BaseFragment
import com.haraev.main.R
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_movie_details.*

class MovieDetailsFragment : BaseFragment(R.layout.fragment_movie_details) {

    private val args by navArgs<MovieDetailsFragmentArgs>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        sharedElementEnterTransition = TransitionInflater.from(requireContext())
            .inflateTransition(android.R.transition.move)

        initViews()
    }

    private fun initViews() {
        setupArgsContent()
        setupBackButton()
    }

    private fun setupBackButton() {
        movie_details_back_button.setOnClickListener {
            findNavController().navigateUp()
        }
    }

    private fun setupArgsContent() {
        with(args) {
            moviePosterPath?.let {
                Picasso.get()
                    .load("$BASE_IMAGE_URL$it")
                    .into(movie_details_image)
            }
            movie_details_title_text_view.text = movieTitle
            val originalTitleWithYear = resources.getString(
                R.string.original_title_with_date,
                movieOriginalTitle,
                movieReleaseDate
            )
            movie_details_original_title_with_year_text.text = originalTitleWithYear
            movie_details_genres_text.text = movieGenres ?: ""
            movie_details_duration_content_text.text = movieDuration ?: ""
            movie_details_rating_content_vote_average_text.text = movieVoteAverage.toString()
            movie_details_rating_content_vote_count_text.text = movieVoteCount.toString()
            movie_details_overview.text = movieOverview
        }
    }
}