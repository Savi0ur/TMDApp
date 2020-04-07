package com.haraev.main.presentation.item

import android.widget.ImageView
import android.widget.TextView
import com.haraev.main.data.model.MovieUi

interface MovieItem {

    val movie: MovieUi

    var movieImageView: ImageView
    var movieTitleView: TextView
    var movieOriginalTitleView: TextView
    var movieGenresView: TextView
    var movieDurationView: TextView
    var movieVoteAverageView: TextView
    var movieVoteCountView: TextView
}