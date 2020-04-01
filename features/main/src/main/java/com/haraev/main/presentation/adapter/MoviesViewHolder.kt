package com.haraev.main.presentation.adapter

import android.view.View
import androidx.navigation.fragment.FragmentNavigatorExtras
import com.haraev.core.ui.BaseViewHolder
import com.haraev.main.data.model.Movie
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.item_movie_linear.*

class MoviesViewHolder(
    itemView: View,
    private val itemClickListener: MoviesAdapter.ItemClickListener?
) : BaseViewHolder(itemView) {

    fun bind(movie: Movie) {
        movie.poster_path?.let {
            Picasso.get()
                .load("$BASE_IMAGE_URL$it")
                .resize(64, 96)
                .into(movie_image)
        }
        movie_title.text = movie.title
        movie_original_title_with_year.text = "${movie.originalTitle} (${movie.releaseDate})"
        movie_vote_average.text = movie.voteAverage.toString()
        movie_vote_count.text = movie.voteCount.toString()
        movie_image.setOnClickListener {
            itemClickListener?.onItemClick(
                movie = movie,
                extras = FragmentNavigatorExtras(
                    movie_title to "movie_title"
                )
            )
        }
    }

    companion object {
        private const val BASE_IMAGE_URL = "https://image.tmdb.org/t/p/w185"
    }

}