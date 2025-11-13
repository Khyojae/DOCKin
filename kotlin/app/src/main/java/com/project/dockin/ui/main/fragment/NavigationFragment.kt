package com.project.dockin.ui.fragment

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.project.dockin.R

/**
 * AR 네비게이션 / 팝업 메모 탭
 * - Unity + ARCore + YOLO 연동 전까지는 토스트만
 */
class NavigationFragment : Fragment(R.layout.fragment_navigation) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val btnArNav   = view.findViewById<Button>(R.id.btnArNavigation)
        val btnArMemo  = view.findViewById<Button>(R.id.btnArMemo)

        btnArNav.setOnClickListener {
            Toast.makeText(requireContext(), "AR 네비게이션 (Unity/ARCore) 연동 예정", Toast.LENGTH_SHORT).show()
        }

        btnArMemo.setOnClickListener {
            Toast.makeText(requireContext(), "YOLO+AR 메모 모드 연동 예정", Toast.LENGTH_SHORT).show()
        }
    }
}