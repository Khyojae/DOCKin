package com.project.dockin.ui.chat

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.project.dockin.R

data class BotUiMessage(
    val role: String,  // "user" or "assistant"
    val text: String
)

class BotChatAdapter(
    private val items: MutableList<BotUiMessage>
) : RecyclerView.Adapter<BotChatAdapter.VH>() {

    class VH(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val tvText = itemView.findViewById<TextView>(R.id.tvText)

        fun bind(item: BotUiMessage) {
            tvText.text = item.text
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val v = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_bot_message, parent, false)
        return VH(v)
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount(): Int = items.size

    fun addMessage(msg: BotUiMessage) {
        items.add(msg)
        notifyItemInserted(items.size - 1)
    }
}