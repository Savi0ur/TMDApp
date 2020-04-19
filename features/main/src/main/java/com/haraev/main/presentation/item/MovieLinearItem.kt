package com.haraev.main.presentation.item

import com.haraev.main.R
import com.haraev.main.data.model.response.MovieDetailsResponse

data class MovieLinearItem(
    override val movie: MovieDetailsResponse
) : MovieItem(movie) {

    override fun getLayout(): Int = R.layout.item_movie_linear
}