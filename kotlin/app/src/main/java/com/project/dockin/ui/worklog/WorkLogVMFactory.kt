package com.project.dockin.ui.worklog

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.project.dockin.data.repo.WorkLogRepository

class WorkLogVMFactory(
    private val repo: WorkLogRepository
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(WorkLogViewModel::class.java)) {
            return WorkLogViewModel(repo) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
    }
}