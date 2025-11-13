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
        val locals = remote.map { r ->
            SafetyCourseLocal(
                courseId        = r.courseId,                 // Int (nullable이면 ?: 0)
                title           = r.title,                    // String
                description     = r.description ?: "",        // String?
                videoUrl        = r.videoUrl ?: "",           // String?
                durationMinutes = r.durationMinutes ?: 0,     // Int?
                isMandatory     = r.isMandatory ?: true,      // Boolean?
                createdAt       = r.createdAt ?: ""           // 널 가드
            )
        }
        db.safetyCourseDao().upsertAll(locals)
    }
    suspend fun enroll(courseId: Int) = api.enroll(courseId)
}