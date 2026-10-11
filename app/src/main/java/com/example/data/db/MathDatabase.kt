package com.example.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.data.model.BookmarkEntity
import com.example.data.model.ProgressEntity
import com.example.data.model.QuizScoreEntity

@Database(
    entities = [BookmarkEntity::class, QuizScoreEntity::class, ProgressEntity::class],
    version = 1,
    exportSchema = false
)
abstract class MathDatabase : RoomDatabase() {
    abstract fun mathDao(): MathDao

    companion object {
        @Volatile
        private var INSTANCE: MathDatabase? = null

        fun getInstance(context: Context): MathDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    MathDatabase::class.java,
                    "math_master.db"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}
