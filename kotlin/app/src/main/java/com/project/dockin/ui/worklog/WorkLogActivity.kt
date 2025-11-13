package com.project.dockin.ui.worklog

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.project.dockin.R
import com.project.dockin.data.api.Network
import com.project.dockin.data.api.WorkLogApi
import com.project.dockin.data.repo.WorkLogRepository

class WorkLogActivity : AppCompatActivity() {

    private val viewModel: WorkLogViewModel by viewModels {
        val retrofit = Network.retrofit(this@WorkLogActivity)
        val api = retrofit.create(WorkLogApi::class.java)
        val repo = WorkLogRepository(api)
        WorkLogVMFactory(repo)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_worklog)

        val etTitle = findViewById<EditText>(R.id.etTitle)
        val etContent = findViewById<EditText>(R.id.etContent)
        val btnSave = findViewById<Button>(R.id.btnSave)

        btnSave.setOnClickListener {
            val title = etTitle.text.toString()
            val body = etContent.text.toString()

            if (title.isBlank() || body.isBlank()) {
                Toast.makeText(this, "제목과 내용을 입력해주세요.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            viewModel.create(
                title = title,
                logText = body,
                equipmentId = null,
                audioUrl = null
            ) { ok, msg ->
                if (ok) {
                    Toast.makeText(this, "작업일지 등록 완료", Toast.LENGTH_SHORT).show()
                    finish()
                } else {
                    Toast.makeText(this, "실패: ${msg ?: "알 수 없음"}", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}