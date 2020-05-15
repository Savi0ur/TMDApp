package com.haraev.database.dao

import androidx.room.*
import com.haraev.database.entity.MovieDb
import io.reactivex.Completable
import io.reactivex.Single

@Dao
interface MovieDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertMovies(vararg movies: MovieDb): Completable

    @Query("SELECT * FROM moviedb")
    fun getAll(): Single<List<MovieDb>>

    @Query("SELECT * FROM moviedb WHERE serverId = :id")
    fun getByServerId(id: Int): Single<MovieDb>

    @Query("SELECT * FROM moviedb WHERE isFavorite = 1")
    fun getAllFavorite(): Single<List<MovieDb>>

    @Delete
    fun delete(vararg  movies: MovieDb): Completable

    @Query("DELETE FROM moviedb WHERE serverId = :id")
    fun deleteById(id: Int): Completable
}