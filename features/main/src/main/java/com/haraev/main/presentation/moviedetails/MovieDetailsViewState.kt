package com.haraev.main.presentation.moviedetails

data class MovieDetailsViewState (
    val progressBarVisibility: Boolean = false,
    val isFavoriteMovie: Boolean = false,
    val markAsFavoriteInProcess: Boolean = false
)