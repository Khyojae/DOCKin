package com.project.dockin.data.db

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface SafetyCourseDao {

    @Query("SELECT * FROM safety_courses ORDER BY courseId DESC")
    fun observeAll(): Flow<List<SafetyCourseLocal>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertAll(items: List<SafetyCourseLocal>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(item: SafetyCourseLocal)

    @Query("DELETE FROM safety_courses")
    suspend fun clear()
}