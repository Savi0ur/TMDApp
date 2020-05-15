package com.haraev.database.migration

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

class Migration2to3 : Migration(2, 3) {

    override fun migrate(database: SupportSQLiteDatabase) {

        // 1. Create new table
        database.execSQL("""
            CREATE TABLE IF NOT EXISTS `new_MovieDb` (
                `serverId` INTEGER NOT NULL,
                `duration` INTEGER,
                `posterPath` TEXT,
                `overview` TEXT NOT NULL,
                `releaseDate` TEXT,
                `originalTitle` TEXT NOT NULL,
                `title` TEXT NOT NULL,
                `voteCount` INTEGER NOT NULL,
                `voteAverage` REAL NOT NULL,
                `isFavorite` INTEGER NOT NULL,
                `isWorthWatching` INTEGER NOT NULL,
                PRIMARY KEY(`serverId`)
            )
        """)

        // 2. Copy data
        database.execSQL("""
            INSERT INTO
                `new_MovieDb` (
                `serverId`,
                `duration`,
                `posterPath`,
                `overview`,
                `releaseDate`,
                `originalTitle`,
                `title`,
                `voteCount`,
                `voteAverage`,
                `isFavorite`,
                `isWorthWatching`)
            SELECT
                `serverId`,
                `duration`,
                `posterPath`,
                `overview`,
                `releaseDate`,
                `originalTitle`,
                `title`,
                `voteCount`,
                `voteAverage`,
                `isFavorite`,
            CASE
                WHEN `voteAverage` > 7.0 THEN 1
                ELSE 0
            END
            FROM `MovieDb`
        """)

        // 3. Drop old table
        database.execSQL("""
           DROP TABLE `MovieDb`
        """)

        // 4. Rename new into old
        database.execSQL("""
          ALTER TABLE `new_MovieDb` RENAME TO `MovieDb`
        """)
    }
}