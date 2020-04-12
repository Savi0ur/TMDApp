package com.haraev.core.common

import com.haraev.core.BuildConfig

/**
 * API ключ для запросов к https://api.themoviedb.org/
 */
const val TMDB_API_KEY = BuildConfig.TMDB_API_KEY

/**
 * Базовый URL для запросов
 */
const val TMDB_BASE_URL = "api.themoviedb.org"
const val EXTENDED_TMDB_BASE_URL = "https://$TMDB_BASE_URL/3/"


/**
 * Базовый URL для изображений
 */
const val BASE_IMAGE_URL = "https://image.tmdb.org/t/p/w185"