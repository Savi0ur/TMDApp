package com.haraev.main.presentation.item

import com.haraev.main.R
import com.haraev.main.data.model.MovieUi

data class MovieGridItem(
    override val movie: MovieUi
) : MovieItem(movie) {

    override fun getLayout(): Int = R.layout.item_movie_grid
}