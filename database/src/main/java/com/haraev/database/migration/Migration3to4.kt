package com.haraev.database.migration

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

class Migration3to4 : Migration(3, 4) {

    override fun migrate(database: SupportSQLiteDatabase) {
        database.execSQL("""
            ALTER TABLE `MovieDb`
            ADD COLUMN `genres` TEXT DEFAULT '' NOT NULL
        """)
    }
}