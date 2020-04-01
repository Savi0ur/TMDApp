package com.haraev.main.presentation.moviedetails

import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.navArgs
import androidx.transition.TransitionInflater
import com.haraev.core.ui.BaseFragment
import com.haraev.main.R
import kotlinx.android.synthetic.main.fragment_movie_details.*

class MovieDetailsFragment : BaseFragment(R.layout.fragment_movie_details) {

    private val args by navArgs<MovieDetailsFragmentArgs>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        movie_details_title_text_view.text = args.movieTitle

        sharedElementEnterTransition = TransitionInflater.from(requireContext())
            .inflateTransition(android.R.transition.move)
    }
}