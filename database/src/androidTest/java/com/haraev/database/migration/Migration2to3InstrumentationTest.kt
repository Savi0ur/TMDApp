package com.haraev.database.migration

import org.junit.Rule
import androidx.room.testing.MigrationTestHelper
import androidx.sqlite.db.framework.FrameworkSQLiteOpenHelperFactory
import androidx.test.platform.app.InstrumentationRegistry
import com.haraev.database.Database
import org.junit.Test

class Migration2to3InstrumentationTest {

    @Rule
    @JvmField
    val helper: MigrationTestHelper = MigrationTestHelper(
        InstrumentationRegistry.getInstrumentation(),
        Database::class.java.canonicalName,
        FrameworkSQLiteOpenHelperFactory()
    )

    @Test
    fun migrationFrom1To2_success() {
        val db = helper.createDatabase(TEST_DB_NAME, 2)
        db.close()

        helper.runMigrationsAndValidate(TEST_DB_NAME, 3, true, Migration2to3())
    }

    companion object {
        private const val TEST_DB_NAME = "test_db_name"
    }
}