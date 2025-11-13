package com.project.dockin.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

// ▶ WorkLogLocal 제거, SafetyCourseLocal 만 엔티티로 사용
@Database(
    entities = [SafetyCourseLocal::class],
    version = 2,
    exportSchema = false
)
abstract class AppDb : RoomDatabase() {

    // ▶ workLogDao() 삭제, safetyCourseDao()만 남김
    abstract fun safetyCourseDao(): SafetyCourseDao

    companion object {
        @Volatile
        private var INSTANCE: AppDb? = null

        fun get(context: Context): AppDb =
            INSTANCE ?: synchronized(this) {
                Room.databaseBuilder(
                    context.applicationContext,
                    AppDb::class.java,
                    "app.db"
                )
                    .fallbackToDestructiveMigration()
                    .build()
                    .also { INSTANCE = it }
            }
    }
}