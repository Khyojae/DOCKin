package com.project.dockin.ui.fragment

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.fragment.app.Fragment
import com.project.dockin.R
import com.project.dockin.ui.worklog.WorkLogActivity
import com.project.dockin.ui.worklog.WorkLogListActivity

/**
 * 작업일지 탭 - 작성/목록으로 이동 버튼만 제공
 */
class WorkLogFragment : Fragment(R.layout.fragment_worklog) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val btnWrite = view.findViewById<Button>(R.id.btnWorklogWrite)
        val btnList  = view.findViewById<Button>(R.id.btnWorklogList)

        btnWrite.setOnClickListener {
            startActivity(Intent(requireContext(), WorkLogActivity::class.java))
        }

        btnList.setOnClickListener {
            startActivity(Intent(requireContext(), WorkLogListActivity::class.java))
        }
    }
}