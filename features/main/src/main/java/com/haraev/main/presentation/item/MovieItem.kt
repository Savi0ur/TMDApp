package com.haraev.main.presentation.item

import android.widget.ImageView
import android.widget.TextView
import com.haraev.main.data.model.Movie

interface MovieItem {

    val movie: Movie

    var movieImageView: ImageView
    var movieTitleView: TextView
    var movieOriginalTitleView: TextView
    var movieGenresView: TextView
    var movieDurationView: TextView
    var movieVoteAverageView: TextView
    var movieVoteCountView: TextView
}