package com.project.dockin.ui.chat

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.project.dockin.R
import com.project.dockin.data.chat.ChatMessage

class ChatMessageAdapter : RecyclerView.Adapter<ChatMessageAdapter.VH>() {

    private val items = mutableListOf<ChatMessage>()

    fun submitList(list: List<ChatMessage>) {
        items.clear()
        items.addAll(list)
        notifyDataSetChanged()
    }

    override fun getItemViewType(position: Int): Int {
        return if (items[position].mine) 1 else 0
    }

    class VH(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvText: TextView = itemView.findViewById(R.id.tvText)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val layout = if (viewType == 1) {
            R.layout.item_chat_message_me
        } else {
            R.layout.item_chat_message_other
        }
        val v = LayoutInflater.from(parent.context).inflate(layout, parent, false)
        return VH(v)
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        holder.tvText.text = items[position].text
    }

    override fun getItemCount(): Int = items.size
}