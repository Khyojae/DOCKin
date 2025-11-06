package com.project.dockin.ui.login

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.project.dockin.R
import com.project.dockin.data.api.*
import kotlinx.coroutines.*

class LoginActivity : AppCompatActivity() {
    private val scope = MainScope()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val etId = findViewById<EditText>(R.id.etId)
        val etPw = findViewById<EditText>(R.id.etPw)
        val btn = findViewById<Button>(R.id.btnLogin)

        val retrofit = Network.retrofit(this)
        val api = retrofit.create(AuthApi::class.java)
        val tokenStore = TokenStore(this)

        btn.setOnClickListener {
            val id = etId.text.toString()
            val pw = etPw.text.toString()
            scope.launch {
                runCatching { api.login(LoginReq(id, pw)) }
                    .onSuccess {
                        tokenStore.jwt = it.token
                        Toast.makeText(this@LoginActivity, "로그인 성공!", Toast.LENGTH_SHORT).show()
                        startActivity(android.content.Intent(this@LoginActivity, com.project.dockin.ui.home.HomeActivity::class.java))
                        finish()
                    }
                    .onFailure {
                        Toast.makeText(this@LoginActivity, "실패: ${it.message}", Toast.LENGTH_SHORT).show()
                    }
            }
        }
    }

    override fun onDestroy() { super.onDestroy(); scope.cancel() }
}