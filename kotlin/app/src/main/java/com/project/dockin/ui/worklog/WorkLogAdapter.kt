package com.project.dockin.ui.worklog

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.project.dockin.R
import com.project.dockin.data.api.WorkLogDto

typealias WorkLogClick = (WorkLogDto) -> Unit

class WorkLogAdapter : ListAdapter<WorkLogDto, WorkLogAdapter.VH>(Diff()) {

    var onItemClick: WorkLogClick? = null
    var onItemDelete: WorkLogClick? = null

    class VH(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val title = itemView.findViewById<TextView>(R.id.tvTitle)
        private val meta  = itemView.findViewById<TextView>(R.id.tvMeta)

        fun bind(item: WorkLogDto) {
            title.text = item.title
            val created = item.created_at ?: ""
            meta.text = "#${item.log_id}  •  ${created}\n${item.log_text}"
        }
    }

    class Diff : DiffUtil.ItemCallback<WorkLogDto>() {
        override fun areItemsTheSame(oldItem: WorkLogDto, newItem: WorkLogDto) =
            oldItem.log_id == newItem.log_id

        override fun areContentsTheSame(oldItem: WorkLogDto, newItem: WorkLogDto) =
            oldItem == newItem
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val v = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_worklog, parent, false)
        return VH(v)
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        val item = getItem(position)
        holder.bind(item)
        holder.itemView.setOnClickListener { onItemClick?.invoke(item) }
        holder.itemView.setOnLongClickListener {
            onItemDelete?.invoke(item)
            true
        }
    }
}