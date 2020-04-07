package com.haraev.main.presentation.search

import com.haraev.main.data.model.MovieUi

data class SearchViewState(
    val progressBarVisibility : Boolean = false,
    val movies : List<MovieUi>?
)