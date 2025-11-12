package com.project.dockin.ui.safety

import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.project.dockin.R
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class SafetyListActivity : AppCompatActivity() {
    private val vm: SafetyViewModel by viewModels { SafetyVMFactory(this) }
    private val adapter = SafetyCourseAdapter(
        onClick = { course ->
            // TODO: 동영상 플레이(ExoPlayer) 화면은 추후
            vm.enroll(course.courseId) { ok, msg ->
                Toast.makeText(this,
                    if (ok) "이수 처리 완료" else "실패: $msg",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_safety_list)

        val rv = findViewById<androidx.recyclerview.widget.RecyclerView>(R.id.rv)
        val swipe = findViewById<SwipeRefreshLayout>(R.id.swipe)

        rv.layoutManager = LinearLayoutManager(this)
        rv.adapter = adapter

        lifecycleScope.launch {
            vm.courses.collectLatest { adapter.submitList(it) }
        }

        swipe.setOnRefreshListener {
            lifecycleScope.launch {
                vm.refresh()
                swipe.isRefreshing = false
            }
        }

        // 최초 로드
        vm.refresh()
    }
}