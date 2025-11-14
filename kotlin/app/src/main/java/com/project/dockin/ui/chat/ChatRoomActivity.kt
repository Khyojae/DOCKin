package com.project.dockin.ui.chat

import android.os.Bundle
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.project.dockin.R
import com.project.dockin.data.chat.ChatMessage
import com.project.dockin.data.chat.ChatRepository
import com.project.dockin.ui.common.BaseActivity

class ChatRoomActivity : BaseActivity() {

    companion object {
        const val EXTRA_ROOM_ID = "roomId"
        const val CHATBOT_ROOM_ID = 999
    }

    private lateinit var repo: ChatRepository
    private val adapter = ChatMessageAdapter()
    private var messages = mutableListOf<ChatMessage>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat_room)

        repo = ChatRepository(this)

        val roomId = intent.getIntExtra(EXTRA_ROOM_ID, CHATBOT_ROOM_ID)
        val room = repo.getRoom(roomId)

        val tvTitle = findViewById<TextView>(R.id.tvRoomTitle)
        val rv = findViewById<RecyclerView>(R.id.rvMessages)
        val etMessage = findViewById<EditText>(R.id.etMessage)
        val btnSend = findViewById<ImageButton>(R.id.btnSend)

        tvTitle.text = room?.name ?: "채팅"

        rv.layoutManager = LinearLayoutManager(this).apply {
            stackFromEnd = true
        }
        rv.adapter = adapter

        messages = (room?.messages ?: emptyList()).toMutableList()
        adapter.submitList(messages.toList())
        rv.scrollToPosition(messages.size - 1)

        btnSend.setOnClickListener {
            val text = etMessage.text.toString().trim()
            if (text.isEmpty()) return@setOnClickListener

            val msg = ChatMessage(
                sender = "나",
                mine = true,
                text = text,
                time = ""
            )
            messages.add(msg)
            adapter.submitList(messages.toList())
            rv.scrollToPosition(messages.size - 1)
            etMessage.setText("")
        }
    }
}