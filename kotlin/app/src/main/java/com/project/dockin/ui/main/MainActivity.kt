package com.project.dockin.ui.main

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.project.dockin.R
import com.project.dockin.data.pref.SessionStore
import com.project.dockin.data.pref.UserRole
import com.project.dockin.ui.main.fragment.*

class MainActivity : AppCompatActivity() {

    private lateinit var session: SessionStore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        session = SessionStore(this)
        setContentView(R.layout.activity_main)

        val bottomNav = findViewById<BottomNavigationView>(R.id.bottomNav)

        // 역할에 따라 메뉴 세팅
        when (session.role) {
            UserRole.WORKER -> bottomNav.inflateMenu(R.menu.menu_bottom_worker)
            UserRole.MANAGER -> bottomNav.inflateMenu(R.menu.menu_bottom_manager)
        }

        bottomNav.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.tab_worker_home -> switch(WorkerHomeFragment())
                R.id.tab_manager_home -> switch(ManagerHomeFragment())
                R.id.tab_chat         -> switch(ChatFragment())
                R.id.tab_worklog      -> switch(WorkLogFragment())
                R.id.tab_nav          -> switch(NavigationFragment())
                R.id.tab_safety_learn -> switch(SafetyLearnFragment())
                R.id.tab_safety_admin -> switch(SafetyAdminFragment())
            }
            true
        }

        // 처음 탭 선택
        val firstId = when (session.role) {
            UserRole.WORKER -> R.id.tab_worker_home
            UserRole.MANAGER -> R.id.tab_manager_home
        }
        bottomNav.selectedItemId = firstId
    }

    private fun switch(f: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.container, f)
            .commit()
    }
}