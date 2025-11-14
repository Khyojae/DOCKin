package com.project.dockin.ui.chat

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.project.dockin.R
import com.project.dockin.data.api.AiApi
import com.project.dockin.data.api.Network
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch

class BotChatActivity : AppCompatActivity() {

    private val scope = MainScope()
    private lateinit var aiApi: AiApi

    private lateinit var rv: RecyclerView
    private lateinit var etMessage: EditText
    private lateinit var btnSend: Button

    private lateinit var adapter: BotChatAdapter
    private val messages = mutableListOf<BotUiMessage>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bot_chat)

        aiApi = Network.aiApi()

        rv = findViewById(R.id.rvChat)
        etMessage = findViewById(R.id.etMessage)
        btnSend = findViewById(R.id.btnSend)

        adapter = BotChatAdapter(messages)
        rv.layoutManager = LinearLayoutManager(this)
        rv.adapter = adapter

        btnSend.setOnClickListener {
            val text = etMessage.text.toString().trim()
            if (text.isEmpty()) return@setOnClickListener

            sendToBot(text)
        }
    }

    private fun sendToBot(text: String) {
        // 입력창 비우고 내 메시지 먼저 화면에 추가
        etMessage.setText("")
        adapter.addMessage(BotUiMessage("user", text))
        rv.scrollToPosition(messages.size - 1)

        scope.launch {
            runCatching {
                val req = AiApi.ChatRequest(
                    messages = listOf(
                        AiApi.ChatMessage(
                            role = "user",
                            content = text
                        )
                    ),
                    domain = "shipyard",
                    lang = "ko"
                )
                aiApi.chat(req)
            }.onSuccess { res ->
                adapter.addMessage(BotUiMessage("assistant", res.reply))
                rv.scrollToPosition(messages.size - 1)
            }.onFailure { e ->
                Toast.makeText(
                    this@BotChatActivity,
                    "챗봇 호출 실패: ${e.message}",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        scope.cancel()
    }
}