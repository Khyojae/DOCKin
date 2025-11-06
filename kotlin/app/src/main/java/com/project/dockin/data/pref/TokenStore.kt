package com.project.dockin.data.api

import android.content.Context
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey

class TokenStore(ctx: Context) {
    private val key = MasterKey.Builder(ctx).setKeyScheme(MasterKey.KeyScheme.AES256_GCM).build()
    private val sp = EncryptedSharedPreferences.create(
        ctx, "auth.secure", key,
        EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
        EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
    )
    var jwt: String?
        get() = sp.getString("jwt", null)
        set(v) { sp.edit().putString("jwt", v).apply() }
    fun clear() = sp.edit().clear().apply()
}