package com.marius.team28.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase


@Database(entities = [Crag::class, Route::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun cragDao(): AppDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null
        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this){
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "crag-database"
                )
                    .createFromAsset("crags-database.db")
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}