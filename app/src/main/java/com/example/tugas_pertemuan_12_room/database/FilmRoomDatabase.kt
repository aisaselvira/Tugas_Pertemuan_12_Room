package com.example.tugas_pertemuan_12_room.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

@Database(entities = [Film::class], version = 2, exportSchema = false)

abstract class FilmRoomDatabase : RoomDatabase() {
    abstract fun filmDao(): FilmDao?

    companion object {
        @Volatile
        private var INSTANCE: FilmRoomDatabase? = null
        fun getDatabase(context: Context): FilmRoomDatabase? {
            if (INSTANCE == null) {
                synchronized(FilmRoomDatabase::class.java) {
                    INSTANCE = Room.databaseBuilder(
                        context.applicationContext,
                        FilmRoomDatabase::class.java, "film_database"
                    )
                        .addMigrations(MIGRATION_2_1)
                        .build()
                }
            }
            return INSTANCE
        }

        private val MIGRATION_2_1: Migration = object : Migration(2, 1) {
            override fun migrate(database: SupportSQLiteDatabase) {

            }
        }
    }
}