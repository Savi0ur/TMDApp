package com.haraev.main.presentation.search

import com.haraev.main.data.model.response.MovieDetailsResponse

data class SearchViewState(
    val progressBarVisibility : Boolean = false,
    val movies : List<MovieDetailsResponse>? = null
)