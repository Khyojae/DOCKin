package com.project.dockin.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.project.dockin.R

// 관리자 안전점검 대시보드 (일단 플레이스홀더)
class SafetyAdminFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(
            R.layout.fragment_safety_admin_placeholder,
            container,
            false
        )
    }
}