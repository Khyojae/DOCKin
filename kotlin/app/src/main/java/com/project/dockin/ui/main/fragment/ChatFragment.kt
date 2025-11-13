package com.project.dockin.ui.main.fragment

// 채팅 리스트 (1:1 / 그룹채팅 목록 뼈대)
class ChatFragment : Fragment() {
    override fun onCreateView(i: LayoutInflater, c: ViewGroup?, b: Bundle?): View =
        i.inflate(R.layout.fragment_chat_list, c, false)
}