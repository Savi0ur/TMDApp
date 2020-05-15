package com.haraev.main.presentation.favorite

import com.haraev.main.data.model.response.MovieDetailsResponse

data class FavoriteViewState(
    val progressBarVisibility: Boolean = false,
    val movies: List<MovieDetailsResponse> = emptyList(),
    val searchViewVisibility: Boolean = false,
    val searchQuery: String = ""
)