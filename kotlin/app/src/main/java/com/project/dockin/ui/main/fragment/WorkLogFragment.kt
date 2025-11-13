package com.project.dockin.ui.main.fragment

// 작업일지 목록 (이미 있는 WorkLogListActivity 레이아웃 재사용해도 됨)
class WorkLogFragment : Fragment() {
    override fun onCreateView(i: LayoutInflater, c: ViewGroup?, b: Bundle?): View =
        i.inflate(R.layout.activity_worklog_list, c, false)
    // 나중에 ViewModel 붙이면 끝
}