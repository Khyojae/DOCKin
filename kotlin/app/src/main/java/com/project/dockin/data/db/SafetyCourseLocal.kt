package com.project.dockin.data.db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "safety_courses")
data class SafetyCourseLocal(
    @PrimaryKey val courseId: Int,
    val title: String,
    val description: String,
    val videoUrl: String,
    val durationMinutes: Int,
    val isMandatory: Boolean,
    val createdAt: String
)