package com.project.dockin.ui.fragment

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.project.dockin.R
import com.project.dockin.data.api.AttendanceApi
import com.project.dockin.data.api.Network
import kotlinx.coroutines.launch

/**
 * 근로자 홈 화면 (출근/퇴근 + 오늘 근태 요약)
 */
class WorkerHomeFragment : Fragment(R.layout.fragment_worker_home) {

    private lateinit var api: AttendanceApi

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val retrofit = Network.retrofit(requireContext())
        api = retrofit.create(AttendanceApi::class.java)

        val btnIn   = view.findViewById<Button>(R.id.btnIn)
        val btnOut  = view.findViewById<Button>(R.id.btnOut)
        val tvState = view.findViewById<TextView>(R.id.tvTodayState)

        btnIn.setOnClickListener {
            viewLifecycleOwner.lifecycleScope.launch {
                runCatching { api.clockIn(AttendanceApi.InReq("Office_A")) }
                    .onSuccess {
                        Toast.makeText(requireContext(), "출근: ${it.status}", Toast.LENGTH_SHORT).show()
                        tvState.text = "오늘 상태: ${it.status}"
                    }
                    .onFailure {
                        Toast.makeText(requireContext(), "출근 실패: ${it.message}", Toast.LENGTH_SHORT).show()
                    }
            }
        }

        btnOut.setOnClickListener {
            viewLifecycleOwner.lifecycleScope.launch {
                runCatching { api.clockOut(AttendanceApi.OutReq("사무실 5층")) }
                    .onSuccess {
                        Toast.makeText(requireContext(), "퇴근: ${it.status}", Toast.LENGTH_SHORT).show()
                        tvState.text = "오늘 상태: ${it.status}"
                    }
                    .onFailure {
                        Toast.makeText(requireContext(), "퇴근 실패: ${it.message}", Toast.LENGTH_SHORT).show()
                    }
            }
        }
    }
}