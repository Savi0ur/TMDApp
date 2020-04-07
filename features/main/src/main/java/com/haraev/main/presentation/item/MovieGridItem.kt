package com.haraev.main.presentation.item

import android.widget.TextView
import com.haraev.main.R
import com.haraev.main.data.model.Movie
import com.squareup.picasso.Picasso
import com.xwray.groupie.kotlinandroidextensions.GroupieViewHolder
import com.xwray.groupie.kotlinandroidextensions.Item
import kotlinx.android.synthetic.main.item_movie_linear.*

class MovieGridItem(
    override val movie: Movie
) : Item(movie.serverId.toLong()), MovieItem {

    override lateinit var movieTitleView: TextView

    override fun bind(viewHolder: GroupieViewHolder, position: Int) {
        with(viewHolder) {
            movieTitleView = movie_title
            movie.poster_path?.let {
                Picasso.get()
                    .load("${BASE_IMAGE_URL}$it")
                    .into(movie_image)
            }
            movie_title.text = movie.title

            val originalTitleWithYear = itemView.resources.getString(
                R.string.original_title_with_date,
                movie.originalTitle,
                movie.releaseDate
            )

            movie_original_title_with_year.text = originalTitleWithYear
            movie_vote_average.text = movie.voteAverage.toString()
            movie_vote_count.text = movie.voteCount.toString()
        }
    }

    override fun getLayout(): Int = R.layout.item_movie_grid

    companion object {
        private const val BASE_IMAGE_URL = "https://image.tmdb.org/t/p/w185"
    }
}