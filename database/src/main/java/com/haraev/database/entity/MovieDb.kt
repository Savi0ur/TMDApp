package com.haraev.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
@Entity
data class MovieDb (
    @PrimaryKey
    val serverId: Int,

    val duration: Int? = null,

    val posterPath: String? = null,

    val overview: String,

    val releaseDate: String? = null,

    val originalTitle: String,

    val title: String,

    val voteCount: Int,

    val voteAverage: Double,

    val isFavorite: Boolean = false,

    val isWorthWatching: Boolean = false,

    val genres: String
)