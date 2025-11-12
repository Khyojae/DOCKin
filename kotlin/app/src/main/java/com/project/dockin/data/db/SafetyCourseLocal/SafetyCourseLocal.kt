package com.project.dockin.data.db

import androidx.room.*

@Entity(tableName = "safety_courses")
data class SafetyCourseLocal(
    @PrimaryKey val courseId: Int,
    val title: String,
    val description: String?,
    val videoUrl: String?,
    val durationMinutes: Int,
    val isMandatory: Boolean = true,
    val createdAt: String?
)

@Dao
interface SafetyCourseDao {
    @Query("SELECT * FROM safety_courses ORDER BY courseId DESC")
    fun observeAll(): kotlinx.coroutines.flow.Flow<List<SafetyCourseLocal>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertAll(list: List<SafetyCourseLocal>)

    @Query("DELETE FROM safety_courses")
    suspend fun clear()
}