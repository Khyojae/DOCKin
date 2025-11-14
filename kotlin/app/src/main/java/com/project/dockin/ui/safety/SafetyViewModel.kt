package com.project.dockin.ui.safety

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.project.dockin.data.db.SafetyCourseLocal
import com.project.dockin.data.repo.SafetyRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class SafetyViewModel(
    private val repo: SafetyRepository
) : ViewModel() {

    val courses = repo.coursesFlow.stateIn(
        scope = viewModelScope,
        started = SharingStarted.Lazily,
        initialValue = emptyList<SafetyCourseLocal>()   //  타입 명시
    )

    fun refresh() = viewModelScope.launch { repo.refresh() }

    fun enroll(id: Int, onDone: (Boolean, String?) -> Unit) = viewModelScope.launch {
        runCatching { repo.enroll(id) }
            .onSuccess { onDone(true, null) }
            .onFailure { onDone(false, it.message) }
    }
}