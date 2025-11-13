package com.project.dockin.data.pref

import android.content.Context

class SessionStore(ctx: Context) {
    private val sp = ctx.getSharedPreferences("session", Context.MODE_PRIVATE)

    var role: UserRole
        get() = UserRole.valueOf(sp.getString("role", UserRole.WORKER.name)!!)
        set(value) { sp.edit().putString("role", value.name).apply() }

    var userId: String?
        get() = sp.getString("userId", null)
        set(v) { sp.edit().putString("userId", v).apply() }

    fun clear() = sp.edit().clear().apply()
}