package com.project.dockin.data.repo

import com.project.dockin.data.api.SafetyApi
import com.project.dockin.data.db.AppDb
import com.project.dockin.data.db.SafetyCourseLocal

class SafetyRepository(
    private val api: SafetyApi,
    private val db: AppDb
) {
    val coursesFlow = db.safetyCourseDao().observeAll()

    suspend fun refresh() {
        val remote = api.listCourses()
        val locals = remote.map {
            SafetyCourseLocal(
                courseId = it.courseId,
                title = it.title,
                description = it.description,
                videoUrl = it.videoUrl,
                durationMinutes = it.durationMinutes,
                isMandatory = it.isMandatory ?: true,
                createdAt = it.createdAt
            )
        }
        db.safetyCourseDao().upsertAll(locals)
    }

    suspend fun enroll(courseId: Int) = api.enroll(courseId)
}