package com.haraev.main.presentation.adapter

import android.view.View
import androidx.navigation.fragment.FragmentNavigatorExtras
import com.haraev.core.ui.BaseViewHolder
import com.haraev.main.R
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
                .into(movie_image)
        }
        movie_title_text.text = movie.title

        val originalTitleWithYear = itemView.resources.getString(
            R.string.original_title_with_date,
            movie.originalTitle,
            movie.releaseDate
        )

        movie_original_title_with_year_text.text = originalTitleWithYear
        movie_vote_average_text.text = movie.voteAverage.toString()
        movie_vote_count_text.text = movie.voteCount.toString()
        movie_image.setOnClickListener {
            itemClickListener?.onItemClick(
                movie = movie,
                extras = FragmentNavigatorExtras(
                    movie_title_text to "movie_title"
                )
            )
        }
    }

    companion object {
        private const val BASE_IMAGE_URL = "https://image.tmdb.org/t/p/w185"
    }

}