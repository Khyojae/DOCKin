package com.project.dockin.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [WorkLogLocal::class,SafetyCourseLocal::class], version = 2,exportSchema = false)
abstract class AppDb : RoomDatabase() {
    abstract fun workLogDao(): WorkLogDao
    abstract fun safetyCourseDao(): SafetyCourseDao

    companion object {
        @Volatile
        private var INSTANCE: AppDb? = null

        fun get(context: Context): AppDb =
            INSTANCE ?: synchronized(this) {
                Room.databaseBuilder(context.applicationContext, AppDb::class.java, "app.db")
                    // 개발중이라면 임시로 사용(데이터 날아감). 실서비스는 MIGRATION 작성해!
                    .fallbackToDestructiveMigration()
                    .build()
                    .also { INSTANCE = it }
            }

    }
}