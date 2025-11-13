package com.project.dockin.ui.fragment

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.project.dockin.R

/**
 * 관리자 홈 대시보드 (근태관리 / 안전점검 / 긴급사항 등 진입 버튼만 먼저 구현)
 */
class ManagerHomeFragment : Fragment(R.layout.fragment_manager_home) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val btnAttendance = view.findViewById<Button>(R.id.btnAttendanceSummary)
        val btnSafety     = view.findViewById<Button>(R.id.btnSafetyDashboard)
        val btnEmergency  = view.findViewById<Button>(R.id.btnEmergency)

        btnAttendance.setOnClickListener {
            Toast.makeText(requireContext(), "근태 현황(출근/휴가/병결) 조회 예정", Toast.LENGTH_SHORT).show()
        }

        btnSafety.setOnClickListener {
            Toast.makeText(requireContext(), "안전점검 / 안전교육 관리 화면으로 이동 예정", Toast.LENGTH_SHORT).show()
        }

        btnEmergency.setOnClickListener {
            Toast.makeText(requireContext(), "긴급사항 대응(FCM 공지 발송) 기능 예정", Toast.LENGTH_SHORT).show()
        }
    }
}