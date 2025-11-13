package com.project.dockin.ui.home

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.project.dockin.R
import com.project.dockin.data.api.AttendanceApi
import com.project.dockin.data.api.Network
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch

class HomeActivity : AppCompatActivity() {
    private val scope = MainScope()
    private lateinit var api: AttendanceApi

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        val retrofit = Network.retrofit(this)
        api = retrofit.create(AttendanceApi::class.java)

        // 출근
        findViewById<Button>(R.id.btnIn).setOnClickListener {
            scope.launch {
                runCatching { api.clockIn(AttendanceApi.InReq("Office_A")) }
                    .onSuccess {
                        Toast.makeText(
                            this@HomeActivity,
                            "출근: ${it.status}",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                    .onFailure {
                        Toast.makeText(
                            this@HomeActivity,
                            "출근 실패: ${it.message}",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
            }
        }

        // 퇴근
        findViewById<Button>(R.id.btnOut).setOnClickListener {
            scope.launch {
                runCatching { api.clockOut(AttendanceApi.OutReq("사무실 5층")) }
                    .onSuccess {
                        Toast.makeText(
                            this@HomeActivity,
                            "퇴근: ${it.status}",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                    .onFailure {
                        Toast.makeText(
                            this@HomeActivity,
                            "퇴근 실패: ${it.message}",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
            }
        }

        // 작업일지 작성
        findViewById<Button>(R.id.btnWorklog).setOnClickListener {
            startActivity(Intent(this, com.project.dockin.ui.worklog.WorkLogActivity::class.java))
        }

        // 작업일지 목록
        findViewById<Button>(R.id.btnWorklogList).setOnClickListener {
            startActivity(Intent(this, com.project.dockin.ui.worklog.WorkLogListActivity::class.java))
        }

        // 근태 기록 조회
        findViewById<Button>(R.id.btnAttendanceList).setOnClickListener {
            scope.launch {
                runCatching { api.list() }
                    .onSuccess { list ->
                        if (list.isEmpty()) {
                            Toast.makeText(
                                this@HomeActivity,
                                "근태 기록이 없습니다.",
                                Toast.LENGTH_SHORT
                            ).show()
                        } else {
                            // 가장 마지막 기록 하나만 간단히 보여주자
                            val latest = list.last()
                            val msg = buildString {
                                append("총 ${list.size}건\n")
                                append("마지막 상태: ${latest.status}\n")
                                append("출근: ${latest.clockInTime ?: "-"}\n")
                                append("퇴근: ${latest.clockOutTime ?: "-"}")
                            }
                            Toast.makeText(this@HomeActivity, msg, Toast.LENGTH_LONG).show()
                        }
                    }
                    .onFailure {
                        Toast.makeText(
                            this@HomeActivity,
                            "근태 조회 실패: ${it.message}",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        scope.cancel()
    }
}