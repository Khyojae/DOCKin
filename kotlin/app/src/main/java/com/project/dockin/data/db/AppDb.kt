package com.project.dockin.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [WorkLogLocal::class], version = 1)
abstract class AppDb : RoomDatabase() {
    abstract fun workLogDao(): WorkLogDao
    abstract fun safetyCourseDao(): SafetyCourseDao
    companion object {
        fun get(ctx: Context) =
            Room.databaseBuilder(ctx, AppDb::class.java, "beam.db").build()
    }
}