package com.project.dockin.ui.worklog

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.project.dockin.R
import com.project.dockin.data.api.WorkLogApi
import com.project.dockin.data.api.Network
import com.project.dockin.data.repo.WorkLogRepository
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class WorkLogListActivity : AppCompatActivity() {

    companion object {
        private const val TAG = "WorkLogListActivity"
    }

    private val viewModel: WorkLogViewModel by viewModels {
        val retrofit = Network.retrofit(this@WorkLogListActivity)
        val api = retrofit.create(WorkLogApi::class.java)
        val repo = WorkLogRepository(api)
        WorkLogVMFactory(repo)
    }

    private val adapter = WorkLogAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_worklog_list)

        val rv = findViewById<RecyclerView>(R.id.rv)
        val swipe = findViewById<SwipeRefreshLayout>(R.id.swipe)

        rv.layoutManager = LinearLayoutManager(this)
        rv.adapter = adapter

        // 목록 관찰
        lifecycleScope.launch {
            viewModel.items.collectLatest { list ->
                Log.d(TAG, "collectLatest: size=${list.size}")
                adapter.submitList(list)
            }
        }

        // 당겨서 새로고침
        swipe.setOnRefreshListener {
            Log.d(TAG, "swipe refresh")
            viewModel.refresh()
            swipe.isRefreshing = false
        }

        // 최초 로드
        Log.d(TAG, "onCreate -> refresh()")
        viewModel.refresh()

        adapter.onItemClick = { item ->
            Toast.makeText(this, "선택: #${item.log_id}", Toast.LENGTH_SHORT).show()
        }
        adapter.onItemDelete = { item ->
            viewModel.remove(item.log_id) { ok, msg ->
                Toast.makeText(
                    this,
                    if (ok) "삭제 완료" else "삭제 실패: ${msg ?: "알 수 없음"}",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }
}