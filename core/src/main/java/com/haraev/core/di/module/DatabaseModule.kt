package com.haraev.core.di.module

import android.content.Context
import androidx.room.Room
import com.haraev.database.Database
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(context: Context) : Database {
        return Room.databaseBuilder(
            context.applicationContext,
            Database::class.java,
            DATABASE_NAME
        ).build()
    }

    companion object {
        private const val DATABASE_NAME = "AppDatabase"
    }
}