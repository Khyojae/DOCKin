package com.project.dockin.data.repo

import com.project.dockin.data.api.SafetyApi
import com.project.dockin.data.db.AppDb
import com.project.dockin.data.db.SafetyCourseLocal
import kotlinx.coroutines.flow.Flow

class SafetyRepository(
    private val api: SafetyApi,
    private val db: AppDb
) {
    val coursesFlow: Flow<List<SafetyCourseLocal>> =
        db.safetyCourseDao().observeAll()

    suspend fun refresh() {
        val remote = api.listCourses()
        val locals = remote.map { r ->
            SafetyCourseLocal(
                courseId        = r.courseId,
                title           = r.title,
                description     = r.description ?: "",
                videoUrl        = r.videoUrl ?: "",
                durationMinutes = r.durationMinutes,
                isMandatory     = r.isMandatory,
                createdAt       = r.createdAt ?: ""
            )
        }
        db.safetyCourseDao().upsertAll(locals)
    }

    suspend fun enroll(courseId: Int) = api.enroll(courseId)
}