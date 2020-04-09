package com.haraev.main.presentation.adapter

import androidx.recyclerview.widget.DiffUtil
import com.haraev.main.data.model.Movie

class MoviesDiffUtilsCallback(
    private val oldMoviesList: List<Movie>,
    private val newMoviesList: List<Movie>
) : DiffUtil.Callback(){

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldMoviesList[oldItemPosition].serverId == newMoviesList[newItemPosition].serverId
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldMoviesList[oldItemPosition].hashCode() == newMoviesList[newItemPosition].hashCode()
    }

    override fun getOldListSize(): Int {
        return oldMoviesList.size
    }

    override fun getNewListSize(): Int {
        return newMoviesList.size
    }
}