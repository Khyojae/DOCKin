package com.project.dockin.ui.home

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

        val retrofit = Network.retrofit(this, baseUrl = "http://10.0.2.2:8081/")
        api = retrofit.create(AttendanceApi::class.java)

        findViewById<Button>(R.id.btnIn).setOnClickListener {
            scope.launch {
                runCatching { api.clockIn(AttendanceApi.InReq("Office_A")) }
                    .onSuccess { Toast.makeText(this@HomeActivity, "출근: ${it.status}", Toast.LENGTH_SHORT).show() }
                    .onFailure { Toast.makeText(this@HomeActivity, "출근 실패: ${it.message}", Toast.LENGTH_SHORT).show() }
            }
        }

        findViewById<Button>(R.id.btnOut).setOnClickListener {
            scope.launch {
                runCatching { api.clockOut(AttendanceApi.OutReq("사무실 5층")) }
                    .onSuccess { Toast.makeText(this@HomeActivity, "퇴근: ${it.status}", Toast.LENGTH_SHORT).show() }
                    .onFailure { Toast.makeText(this@HomeActivity, "퇴근 실패: ${it.message}", Toast.LENGTH_SHORT).show() }
            }
        }
    }

    override fun onDestroy() { super.onDestroy(); scope.cancel() }
}