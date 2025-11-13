package com.project.dockin.ui.fragment

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.project.dockin.R

/**
 * 채팅/챗봇 탭
 * - 지금은 더미 데이터만 보여주는 목록
 */
class ChatFragment : Fragment(R.layout.fragment_chat) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val rv = view.findViewById<RecyclerView>(R.id.rvChatList)
        rv.layoutManager = LinearLayoutManager(requireContext())
        val adapter = SimpleStringAdapter { roomName ->
            Toast.makeText(requireContext(), "채팅방 '$roomName' - 나중에 WebSocket/LLM 붙이기", Toast.LENGTH_SHORT).show()
        }
        rv.adapter = adapter

        adapter.submitList(
            listOf(
                "관리자 공지방",
                "제 8조선소 (용접팀)",
                "1002 Anh Minh",
                "1003 박민준",
                "DOCKin 작업도우미 챗봇"
            )
        )
    }

    // 아주 간단한 String 리스트 어댑터
    private class SimpleStringAdapter(
        private val onClick: (String) -> Unit
    ) : RecyclerView.Adapter<SimpleVH>() {

        private var items: List<String> = emptyList()

        fun submitList(newItems: List<String>) {
            items = newItems
            notifyDataSetChanged()
        }

        override fun onCreateViewHolder(parent: android.view.ViewGroup, viewType: Int): SimpleVH {
            val v = android.view.LayoutInflater.from(parent.context)
                .inflate(android.R.layout.simple_list_item_1, parent, false)
            return SimpleVH(v, onClick)
        }

        override fun onBindViewHolder(holder: SimpleVH, position: Int) {
            holder.bind(items[position])
        }

        override fun getItemCount(): Int = items.size
    }

    private class SimpleVH(
        itemView: View,
        private val onClick: (String) -> Unit
    ) : RecyclerView.ViewHolder(itemView) {
        private val tv = itemView.findViewById<android.widget.TextView>(android.R.id.text1)

        fun bind(text: String) {
            tv.text = text
            itemView.setOnClickListener { onClick(text) }
        }
    }
}