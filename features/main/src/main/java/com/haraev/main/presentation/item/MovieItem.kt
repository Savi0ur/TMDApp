package com.haraev.main.presentation.item

import android.widget.TextView
import com.haraev.main.data.model.Movie

interface MovieItem {

    val movie: Movie
    var movieTitleView: TextView

}