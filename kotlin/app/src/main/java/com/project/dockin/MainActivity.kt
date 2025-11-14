package com.project.dockin

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.project.dockin.R
import com.project.dockin.ui.fragment.ChatFragment
import com.project.dockin.ui.fragment.ManagerHomeFragment
import com.project.dockin.ui.fragment.NavigationFragment
import com.project.dockin.ui.fragment.SafetyAdminFragment
import com.project.dockin.ui.fragment.SafetyLearnFragment
import com.project.dockin.ui.fragment.WorkLogFragment
import com.project.dockin.ui.fragment.WorkerHomeFragment
import com.project.dockin.util.JwtUtils

class MainActivity : AppCompatActivity() {

    private lateinit var bottomNav: BottomNavigationView
    private var isManager: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        bottomNav = findViewById(R.id.bottomNav)

        // 1) 토큰에서 권한 파싱 (없으면 기본 근로자)
        val token = intent.getStringExtra("accessToken")
            ?: getSharedPreferences("auth", MODE_PRIVATE)
                .getString("accessToken", null)

        val payload = token?.let { JwtUtils.parse(it) }
        // 백엔드에서 ROLE_ADMIN / ROLE_MANAGER 이런 식으로 줄 거라고 가정
        isManager = payload?.auth?.contains("ADMIN") == true

        // 2) 권한에 따라 다른 메뉴 사용
        bottomNav.menu.clear()
        bottomNav.inflateMenu(
            if (isManager) R.menu.menu_bottom_manager
            else R.menu.menu_bottom_worker
        )

        // 3) 최초 화면 설정
        if (savedInstanceState == null) {
            val startFragment: Fragment =
                if (isManager) ManagerHomeFragment() else WorkerHomeFragment()
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragmentContainer, startFragment)
                .commit()
        }

        // 4) 탭 클릭 시 프래그먼트 교체
        bottomNav.setOnItemSelectedListener { item ->
            val fragment: Fragment? = when (item.itemId) {
                R.id.menu_tab_home ->
                    if (isManager) ManagerHomeFragment() else WorkerHomeFragment()

                R.id.menu_tab_chat ->
                    ChatFragment()

                R.id.menu_tab_worklog ->
                    WorkLogFragment()

                R.id.menu_tab_nav ->
                    NavigationFragment()

                R.id.menu_tab_safety ->
                    if (isManager) SafetyAdminFragment()   // 관리자: 안전점검 대시보드
                    else SafetyLearnFragment()             // 근로자: 안전교육 이수

                else -> null
            }

            fragment?.let {
                supportFragmentManager.beginTransaction()
                    .replace(R.id.fragmentContainer, it)
                    .commit()
                true
            } ?: false
        }
    }
}