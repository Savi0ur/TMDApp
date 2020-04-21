package com.haraev.database.dao

import androidx.room.Room
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.runner.AndroidJUnit4
import com.haraev.database.Database
import com.haraev.database.entity.MovieDb
import org.assertj.core.api.Assertions.*
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class MovieDaoInstrumentationTest {

    private lateinit var db: Database

    @Before
    fun createDb() {
        db = Room.inMemoryDatabaseBuilder(
            InstrumentationRegistry.getInstrumentation().context.applicationContext,
            Database::class.java
        ).build()
    }

    @After
    fun closeDb() {
        db.close()
    }

    @Test
    fun writeMovieDb() {

        val movieDb = MovieDb (
            serverId = 123,
            duration = 50,
            posterPath = null,
            overview = "random overview",
            releaseDate = "2016-08-26",
            originalTitle = "your lie in april",
            title = "your lie in april",
            voteAverage = 9.0,
            voteCount = 9876,
            isFavorite = true,
            isWorthWatching = false,
            genres = ""
        )

        val insertResult = db.movieDao().insertMovies(movieDb)

        insertResult.test().assertComplete()
    }

    @Test
    fun writeMovieDbAndGetById() {

        val movieDb = MovieDb (
            serverId = 123,
            duration = 50,
            posterPath = null,
            overview = "random overview",
            releaseDate = "2016-08-26",
            originalTitle = "your lie in april",
            title = "your lie in april",
            voteAverage = 9.0,
            voteCount = 9876,
            isFavorite = true,
            isWorthWatching = false,
            genres = ""
        )

        db.movieDao().insertMovies(movieDb).blockingGet()

        val byId = db.movieDao().getByServerId(movieDb.serverId).blockingGet()

        assertThat(byId).isEqualTo(movieDb)
    }

    @Test
    fun writeMoviesDbAndGetAllFavorite() {

        val favoriteMovieDb = MovieDb (
            serverId = 1,
            duration = 50,
            posterPath = null,
            overview = "random overview",
            releaseDate = "2016-08-26",
            originalTitle = "first movie original title",
            title = "first movie title",
            voteAverage = 9.0,
            voteCount = 9876,
            isFavorite = true,
            isWorthWatching = false,
            genres = ""
        )

        val notFavoriteMovieDb = MovieDb (
            serverId = 2,
            duration = 50,
            posterPath = null,
            overview = "random overview",
            releaseDate = "2016-08-26",
            originalTitle = "second movie original title",
            title = "second movie title",
            voteAverage = 9.0,
            voteCount = 9876,
            isFavorite = false,
            isWorthWatching = false,
            genres = ""
        )

        db.movieDao().insertMovies(favoriteMovieDb, notFavoriteMovieDb).blockingGet()

        val allFavorites = db.movieDao().getAllFavorite().blockingGet()

        assertThat(allFavorites).isEqualTo(listOf(favoriteMovieDb))
    }
}