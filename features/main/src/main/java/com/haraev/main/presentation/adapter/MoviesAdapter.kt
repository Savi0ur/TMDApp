package com.haraev.main.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.fragment.FragmentNavigator
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.haraev.main.R
import com.haraev.main.data.model.Movie

class MoviesAdapter : RecyclerView.Adapter<MoviesViewHolder>() {

    private var movies: List<Movie> = emptyList()

    private var itemClickListener: ItemClickListener? = null

    fun setOnItemClickListener(listener: ItemClickListener) {
        itemClickListener = listener
    }

    fun updateMovies(newMovies: List<Movie>) {
        val diffUtilCallback = MoviesDiffUtilsCallback(movies, newMovies)
        val diffUtilResult = DiffUtil.calculateDiff(diffUtilCallback, true)
        diffUtilResult.dispatchUpdatesTo(this)
        movies = newMovies
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MoviesViewHolder {
        val context = parent.context
        val movieItemLayoutId = R.layout.item_movie_linear
        val view = LayoutInflater.from(context).inflate(movieItemLayoutId, parent, false)
        return MoviesViewHolder(view, itemClickListener)
    }

    override fun getItemCount(): Int {
        return movies.size
    }

    override fun onBindViewHolder(holder: MoviesViewHolder, position: Int) {
        holder.bind(movies[position])
    }

    interface ItemClickListener {

    fun onItemClick(movie: Movie, extras: FragmentNavigator.Extras)
    }
}