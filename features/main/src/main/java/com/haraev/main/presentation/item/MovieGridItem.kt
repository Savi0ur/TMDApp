package com.haraev.main.presentation.item

import android.widget.ImageView
import android.widget.TextView
import com.haraev.core.common.BASE_IMAGE_URL
import com.haraev.main.R
import com.haraev.main.data.model.MovieUi
import com.squareup.picasso.Picasso
import com.xwray.groupie.kotlinandroidextensions.GroupieViewHolder
import com.xwray.groupie.kotlinandroidextensions.Item
import kotlinx.android.synthetic.main.item_movie_grid.*

class MovieGridItem(
    override val movie: MovieUi
) : Item(), MovieItem {

    override lateinit var movieTitleView: TextView
    override lateinit var movieImageView: ImageView
    override lateinit var movieOriginalTitleView: TextView
    override lateinit var movieGenresView: TextView
    override lateinit var movieDurationView: TextView
    override lateinit var movieVoteAverageView: TextView
    override lateinit var movieVoteCountView: TextView

    override fun bind(viewHolder: GroupieViewHolder, position: Int) {
        with(viewHolder) {
            movieTitleView = movie_title_text
            movieImageView = movie_image
            movieOriginalTitleView = movie_original_title_with_year_text
            movieGenresView = movie_genres_text
            movieDurationView = movie_duration_context_text
            movieVoteAverageView = movie_vote_average_text
            movieVoteCountView = movie_vote_count_text

            movie.poster_path?.let {
                Picasso.get()
                    .load("${BASE_IMAGE_URL}$it")
                    .placeholder(R.drawable.drawable_search)
                    .into(movie_image)
            } ?: Picasso.get()
                .load(R.drawable.drawable_search)
                .placeholder(R.drawable.drawable_search)
                .into(movie_image)

            movie_title_text.text = movie.title

            val originalTitleWithYear = itemView.resources.getString(
                R.string.original_title_with_date,
                movie.originalTitle,
                movie.releaseDate
            )

            movie_original_title_with_year_text.text = originalTitleWithYear
            movie_vote_average_text.text = movie.voteAverage.toString()
            movie_vote_count_text.text = movie.voteCount.toString()
            movie_duration_context_text.text = movie.duration.toString()
            movie_genres_text.text =
                movie.genres?.joinToString(separator = ", ", limit = 3) { it.name }
        }
    }

    override fun getLayout(): Int = R.layout.item_movie_grid
}