package com.project.dockin.ui.main.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.project.dockin.R
import com.project.dockin.data.api.AttendanceApi
import com.project.dockin.data.api.Network
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch

class WorkerHomeFragment : Fragment() {

    private val scope = MainScope()
    private lateinit var api: AttendanceApi

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        api = Network.retrofit(requireContext()).create(AttendanceApi::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val v = inflater.inflate(R.layout.fragment_worker_home, container, false)

        val tvStatus = v.findViewById<TextView>(R.id.tvStatus)
        val btnIn    = v.findViewById<Button>(R.id.btnIn)
        val btnOut   = v.findViewById<Button>(R.id.btnOut)

        btnIn.setOnClickListener {
            scope.launch {
                runCatching { api.clockIn(AttendanceApi.InReq("Office_A")) }
                    .onSuccess {
                        tvStatus.text = "현재 상태: ${it.status}"
                        Toast.makeText(requireContext(), "출근: ${it.status}", Toast.LENGTH_SHORT).show()
                    }
                    .onFailure {
                        Toast.makeText(requireContext(), "출근 실패: ${it.message}", Toast.LENGTH_SHORT).show()
                    }
            }
        }

        btnOut.setOnClickListener {
            scope.launch {
                runCatching { api.clockOut(AttendanceApi.OutReq("사무실 5층")) }
                    .onSuccess {
                        tvStatus.text = "현재 상태: ${it.status}"
                        Toast.makeText(requireContext(), "퇴근: ${it.status}", Toast.LENGTH_SHORT).show()
                    }
                    .onFailure {
                        Toast.makeText(requireContext(), "퇴근 실패: ${it.message}", Toast.LENGTH_SHORT).show()
                    }
            }
        }

        return v
    }

    override fun onDestroyView() {
        super.onDestroyView()
        scope.cancel()
    }
}