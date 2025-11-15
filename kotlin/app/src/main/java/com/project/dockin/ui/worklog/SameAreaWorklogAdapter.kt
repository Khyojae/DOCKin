package com.project.dockin.ui.worklog

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.project.dockin.R
import com.project.dockin.data.api.WorkLogDto

class SameAreaWorklogAdapter(
    private var items: List<WorkLogDto>,
    private val onClick: (WorkLogDto) -> Unit
) : RecyclerView.Adapter<SameAreaWorklogAdapter.VH>() {

    inner class VH(view: View) : RecyclerView.ViewHolder(view) {
        val tvTitle: TextView = view.findViewById(R.id.tvSameAreaItemTitle)
        val tvPreview: TextView = view.findViewById(R.id.tvSameAreaItemPreview)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val v = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_same_area_worklog, parent, false)
        return VH(v)
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        val item = items[position]

        // user_id 가 null 일 수 있으니까 안전하게 처리
        val user = item.user_id ?: ""

        holder.tvTitle.text = if (user.isNotBlank()) {
            "${item.title}  •  $user"
        } else {
            item.title
        }

        // 내용 앞 50자 미리보기
        holder.tvPreview.text = item.log_text.take(50)

        holder.itemView.setOnClickListener { onClick(item) }
    }

    override fun getItemCount(): Int = items.size

    fun submitList(newItems: List<WorkLogDto>) {
        items = newItems
        notifyDataSetChanged()
    }
}