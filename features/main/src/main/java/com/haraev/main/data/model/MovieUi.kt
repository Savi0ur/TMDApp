package com.haraev.main.data.model

data class MovieUi(

    val poster_path : String?,

    val overview : String,

    val releaseDate : String?,

    val serverId : Int,

    val originalTitle : String,

    val title : String,

    val voteCount : Int,

    val voteAverage : Double,

    val duration: Int?,

    val genres: List<Genre>?
)