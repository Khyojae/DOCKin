package com.project.dockin.ui.ar

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.project.dockin.R

// 추후 Unity + YOLO 팝업 메모 화면으로 교체 예정
class ArMemoActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ar_memo)
    }
}