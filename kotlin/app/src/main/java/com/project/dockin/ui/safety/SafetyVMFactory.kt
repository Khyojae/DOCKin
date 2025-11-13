package com.project.dockin.ui.safety

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.project.dockin.data.api.Network
import com.project.dockin.data.api.SafetyApi
import com.project.dockin.data.db.AppDb
import com.project.dockin.data.repo.SafetyRepository

class SafetyVMFactory(
    private val context: Context
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SafetyViewModel::class.java)) {
            // Retrofit + Room + Repository 조립
            val retrofit = Network.retrofit(context)
            val api = retrofit.create(SafetyApi::class.java)
            val db = AppDb.get(context.applicationContext)
            val repo = SafetyRepository(api, db)
            return SafetyViewModel(repo) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
    }
}