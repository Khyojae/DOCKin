package com.project.dockin.ui.fragment

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.project.dockin.R
import com.project.dockin.data.chat.ChatRepository
import com.project.dockin.data.chat.ChatRoom
import com.project.dockin.ui.chat.ChatRoomActivity

class ChatFragment : Fragment(R.layout.fragment_chat) {

    private lateinit var repo: ChatRepository
    private lateinit var adapter: com.project.dockin.ui.chat.ChatRoomListAdapter
    private var allRooms: List<ChatRoom> = emptyList()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        repo = ChatRepository(requireContext())
        adapter = com.project.dockin.ui.chat.ChatRoomListAdapter { room ->
            // ★ 여기 분기 추가
            if (room.id == com.project.dockin.ui.chat.ChatRoomActivity.CHATBOT_ROOM_ID) {
                // 챗봇 방 → BotChatActivity 사용
                val intent = Intent(requireContext(), com.project.dockin.ui.chat.BotChatActivity::class.java)
                startActivity(intent)
            } else {
                // 일반 방 → 기존 ChatRoomActivity
                val intent = Intent(requireContext(), com.project.dockin.ui.chat.ChatRoomActivity::class.java).apply {
                    putExtra(ChatRoomActivity.EXTRA_ROOM_ID, room.id)
                }
                startActivity(intent)
            }
        }

        val rv = view.findViewById<RecyclerView>(R.id.rvChatList)
        val etSearch = view.findViewById<EditText>(R.id.etSearch)

        rv.layoutManager = LinearLayoutManager(requireContext())
        rv.adapter = adapter

        allRooms = repo.getRooms()
        adapter.submitList(allRooms)

        etSearch.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {}
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val query = s?.toString().orEmpty()
                val filtered = if (query.isBlank()) {
                    allRooms
                } else {
                    allRooms.filter { it.name.contains(query, ignoreCase = true) }
                }
                adapter.submitList(filtered)
            }
        })
    }
}