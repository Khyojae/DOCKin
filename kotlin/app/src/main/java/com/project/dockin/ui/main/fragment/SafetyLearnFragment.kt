package com.project.dockin.ui.fragment

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.project.dockin.R
import com.project.dockin.ui.safety.SafetyListActivity

/**
 * 안전교육 / 이수 현황 탭
 */
class SafetyLearnFragment : Fragment(R.layout.fragment_safety_learn) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val btnLearnList = view.findViewById<Button>(R.id.btnSafetyLearnList)
        val btnAdminView = view.findViewById<Button>(R.id.btnSafetyAdmin)

        btnLearnList.setOnClickListener {
            startActivity(Intent(requireContext(), SafetyListActivity::class.java))
        }

        btnAdminView.setOnClickListener {
            Toast.makeText(requireContext(), "관리자용 안전점검/이수현황 대시보드 예정", Toast.LENGTH_SHORT).show()
        }
    }
}