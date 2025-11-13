package com.project.dockin.data.pref

enum class UserRole {
    WORKER,   // 근로자
    MANAGER;  // 관리자

    companion object {
        fun fromAuth(auth: String?): UserRole =
            when {
                auth?.contains("ADMIN", ignoreCase = true) == true -> MANAGER
                else -> WORKER
            }
    }
}