package com.example.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(
    entities = [DesignRequest::class, SavedPalette::class],
    version = 1,
    exportSchema = false
)
abstract class GraphicsDatabase : RoomDatabase() {
    abstract fun graphicsDao(): GraphicsDao

    companion object {
        @Volatile
        private var INSTANCE: GraphicsDatabase? = null

        fun getDatabase(context: Context): GraphicsDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    GraphicsDatabase::class.java,
                    "hariom_graphics_db"
                )
                .fallbackToDestructiveMigration() // safe for early releases/prototyping
                .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
