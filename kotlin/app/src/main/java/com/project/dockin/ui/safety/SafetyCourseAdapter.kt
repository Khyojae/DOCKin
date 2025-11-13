package com.project.dockin.ui.safety

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.project.dockin.R
import com.project.dockin.data.db.SafetyCourseLocal

class SafetyCourseAdapter(
    private val onClick: (SafetyCourseLocal) -> Unit
) : ListAdapter<SafetyCourseLocal, SafetyCourseAdapter.VH>(Diff()) {

    class VH(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val title = itemView.findViewById<TextView>(R.id.tvTitle)
        private val desc  = itemView.findViewById<TextView>(R.id.tvDesc)
        private val meta  = itemView.findViewById<TextView>(R.id.tvMeta)

        fun bind(item: SafetyCourseLocal, onClick: (SafetyCourseLocal) -> Unit) {
            title.text = item.title
            desc.text  = item.description
            meta.text  = "${item.durationMinutes}분 • ${if (item.isMandatory) "필수" else "선택"}"

            itemView.setOnClickListener { onClick(item) }
        }
    }

    class Diff : DiffUtil.ItemCallback<SafetyCourseLocal>() {
        override fun areItemsTheSame(oldItem: SafetyCourseLocal, newItem: SafetyCourseLocal) =
            oldItem.courseId == newItem.courseId

        override fun areContentsTheSame(oldItem: SafetyCourseLocal, newItem: SafetyCourseLocal) =
            oldItem == newItem
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val v = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_safety_course, parent, false)
        return VH(v)
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        holder.bind(getItem(position), onClick)
    }
}