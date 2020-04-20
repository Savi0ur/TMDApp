package com.haraev.database.migration

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

class Migration1to2 : Migration(1, 2) {

    override fun migrate(database: SupportSQLiteDatabase) {
        database.execSQL("""
            ALTER TABLE `MovieDb`
            ADD COLUMN `isWatched` INTEGER DEFAULT 0 NOT NULL
        """.trimIndent())
    }
}
