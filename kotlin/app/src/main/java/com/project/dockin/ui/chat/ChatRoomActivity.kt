package com.project.dockin.ui.chat

import android.os.Bundle
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.project.dockin.R
import com.project.dockin.data.api.AiApi
import com.project.dockin.data.api.Network
import com.project.dockin.data.chat.ChatMessage
import com.project.dockin.data.chat.ChatRepository
import com.project.dockin.ui.common.BaseActivity
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch

class ChatRoomActivity : BaseActivity() {

    companion object {
        const val EXTRA_ROOM_ID = "roomId"
        const val CHATBOT_ROOM_ID = 999   // 챗봇 방
    }

    private lateinit var repo: ChatRepository
    private val adapter = ChatMessageAdapter()
    private var messages = mutableListOf<ChatMessage>()

    // 챗봇용
    private val scope = MainScope()
    private lateinit var aiApi: AiApi
    private var isBotRoom: Boolean = false
    private var roomId: Int = CHATBOT_ROOM_ID

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat_room)

        repo = ChatRepository(this)

        roomId = intent.getIntExtra(EXTRA_ROOM_ID, CHATBOT_ROOM_ID)
        val room = repo.getRoom(roomId)
        isBotRoom = (roomId == CHATBOT_ROOM_ID)

        if (isBotRoom) {
            aiApi = Network.aiApi()
        }

        val tvTitle = findViewById<TextView>(R.id.tvRoomTitle)
        val rv = findViewById<RecyclerView>(R.id.rvMessages)
        val etMessage = findViewById<EditText>(R.id.etMessage)
        val btnSend = findViewById<ImageButton>(R.id.btnSend)

        tvTitle.text = room?.name ?: if (isBotRoom) "작업 도우미 챗봇" else "채팅"

        rv.layoutManager = LinearLayoutManager(this).apply {
            stackFromEnd = true
        }
        rv.adapter = adapter

        messages = (room?.messages ?: emptyList()).toMutableList()
        adapter.submitList(messages.toList())
        if (messages.isNotEmpty()) {
            rv.scrollToPosition(messages.size - 1)
        }

        btnSend.setOnClickListener {
            val text = etMessage.text.toString().trim()
            if (text.isEmpty()) return@setOnClickListener

            // 내가 보낸 메시지 먼저 추가
            val myMsg = ChatMessage(
                sender = "나",
                mine = true,
                text = text,
                time = ""
            )
            messages.add(myMsg)
            adapter.submitList(messages.toList())
            rv.scrollToPosition(messages.size - 1)
            etMessage.setText("")

            // 챗봇 방이면 서버로 보냄
            if (isBotRoom) {
                sendToBot(text, rv)
            }
        }
    }

    /** FastAPI /api/chat 호출해서 챗봇 답변 받기 */
    private fun sendToBot(userText: String, rv: RecyclerView) {
        scope.launch {
            runCatching {
                val req = AiApi.ChatRequest(
                    messages = listOf(
                        AiApi.ChatMessage(
                            role = "user",
                            content = userText
                        )
                    ),
                    domain = "shipyard",
                    lang = "ko"
                )
                aiApi.chat(req)
            }.onSuccess { res ->
                val botMsg = ChatMessage(
                    sender = "챗봇",
                    mine = false,
                    text = res.reply,
                    time = ""
                )
                messages.add(botMsg)
                adapter.submitList(messages.toList())
                rv.scrollToPosition(messages.size - 1)
            }.onFailure { e ->
                Toast.makeText(
                    this@ChatRoomActivity,
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