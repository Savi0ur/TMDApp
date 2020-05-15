package com.haraev.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.haraev.database.dao.MovieDao
import com.haraev.database.entity.MovieDb

@Database(
    entities = [
        MovieDb::class
    ],
    version = 4
)
abstract class Database : RoomDatabase() {

    abstract fun movieDao(): MovieDao
}