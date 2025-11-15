package com.project.dockin.ui.worklog

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.*
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.project.dockin.R
import com.project.dockin.data.api.AiApi
import com.project.dockin.data.api.Network
import com.project.dockin.data.api.WorkLogApi
import com.project.dockin.data.api.WorkLogDto
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.InputStream
import android.graphics.Color

class WorkLogActivity : AppCompatActivity() {

    private val scope = MainScope()
    private lateinit var aiApi: AiApi
    private lateinit var workLogApi: WorkLogApi

    // UI
    private lateinit var etTitle: EditText
    private lateinit var etContent: EditText
    private lateinit var etTranslated: EditText
    private lateinit var btnStt: Button
    private lateinit var btnTranslate: Button
    private lateinit var spSourceLang: Spinner
    private lateinit var spTargetLang: Spinner
    private lateinit var tabWrite: TextView
    private lateinit var tabTranslate: TextView
    private lateinit var layoutWrite: View
    private lateinit var layoutTranslate: View
    private lateinit var sameAreaAdapter: SameAreaWorklogAdapter

    // 녹음 결과 받는 launcher (마이크 → 파일)
    private val sttLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                val uri: Uri? = result.data?.data
                if (uri != null) {
                    uploadForStt(uri)      // === ① STT 서버로 전송 ===
                } else {
                    Toast.makeText(this, "녹음 파일을 찾을 수 없습니다.", Toast.LENGTH_SHORT).show()
                }
            }
        }

    private val PICK_AUDIO = 1001

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_worklog)

        // --- Retrofit 준비 ---
        aiApi = Network.aiApi()
        workLogApi = Network.retrofit(this).create(WorkLogApi::class.java)

        // --- findViewById (여기까지 맞으면 Unresolved reference 사라짐) ---
        tabWrite = findViewById(R.id.tabWrite)
        tabTranslate = findViewById(R.id.tabTranslate)
        layoutWrite = findViewById(R.id.layoutWrite)
        layoutTranslate = findViewById(R.id.layoutTranslate)

        etTitle = findViewById(R.id.etTitle)
        etContent = findViewById(R.id.etContent)
        etTranslated = findViewById(R.id.etTranslated)
        btnStt = findViewById(R.id.btnStt)
        btnTranslate = findViewById(R.id.btnTranslate)
        spSourceLang = findViewById(R.id.spSourceLang)
        spTargetLang = findViewById(R.id.spTargetLang)

        // === 같은 구역 작업일지 RecyclerView ===
        val rvSame = findViewById<RecyclerView>(R.id.rvSameAreaWorklogs)
        sameAreaAdapter = SameAreaWorklogAdapter(emptyList()) { clicked ->
            etTitle.setText(clicked.title)
            etContent.setText(clicked.log_text)
            tabTranslate.performClick()
        }
        rvSame.layoutManager = LinearLayoutManager(this)
        rvSame.adapter = sameAreaAdapter

        loadSameAreaWorklogs()   // 진짜 장비 id 생기면 거기 연결
        // 인텐트에서 구역 ID 받아오기 (없으면 -1)
        val workAreaId = intent.getLongExtra("workAreaId", -1L)
        if (workAreaId > 0) {
            loadSameAreaWorklogs()
        }

        // === 탭 전환 ===
        fun showWriteTab() {
            layoutWrite.visibility = View.VISIBLE
            layoutTranslate.visibility = View.GONE
            tabWrite.setBackgroundColor(Color.WHITE)
            tabTranslate.setBackgroundColor(Color.LTGRAY)
        }

        fun showTranslateTab() {
            layoutWrite.visibility = View.GONE
            layoutTranslate.visibility = View.VISIBLE
            tabWrite.setBackgroundColor(Color.LTGRAY)
            tabTranslate.setBackgroundColor(Color.WHITE)

            // 번역 탭 들어올 때마다 원문 미리보기 갱신
            val tvSourceText = findViewById<TextView>(R.id.tvSourceText)
            tvSourceText.text = etContent.text.toString()
        }

        tabWrite.setOnClickListener { showWriteTab() }
        tabTranslate.setOnClickListener { showTranslateTab() }
        showWriteTab()

        // === ① STT 버튼: 마이크 → 파일 → /api/worklogs/stt ===
        btnStt.text = "마이크로 작업일지 작성 (STT)"
        btnStt.setOnClickListener {
            val intent = Intent(MediaStore.Audio.Media.RECORD_SOUND_ACTION)

            // 실제 기기: 녹음 앱이 있으면 그걸 사용
            val recorderExist = intent.resolveActivity(packageManager) != null
            if (recorderExist) {
                sttLauncher.launch(intent)
            } else {
                // 에뮬레이터 등: 오디오 파일 선택으로 대체
                Toast.makeText(
                    this,
                    "녹음 앱을 찾을 수 없습니다. 대신 파일을 선택해 주세요.",
                    Toast.LENGTH_SHORT
                ).show()
                startAudioPicker()
            }
        }

        // === ② 번역 버튼: /api/translate ===
        btnTranslate.setOnClickListener {
            val text = etContent.text.toString()
            if (text.isBlank()) {
                Toast.makeText(this, "번역할 내용을 입력하세요.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val srcCode = spinnerToLangCode(spSourceLang.selectedItem?.toString())
            val tgtCode = spinnerToLangCode(spTargetLang.selectedItem?.toString())

            scope.launch {
                runCatching {
                    val body = AiApi.TranslateRequest(
                        text = text,
                        source = srcCode,
                        target = tgtCode
                    )
                    aiApi.translate(body)
                }.onSuccess { res ->
                    etTranslated.setText(res.translated)
                }.onFailure { e ->
                    Toast.makeText(
                        this@WorkLogActivity,
                        "번역 실패: ${e.message}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }

    // ===================== STT 업로드 =====================
    private fun uploadForStt(uri: Uri) {
        scope.launch {
            runCatching {
                val input: InputStream? = contentResolver.openInputStream(uri)
                val bytes = input?.readBytes() ?: ByteArray(0)
                input?.close()

                val body = bytes.toRequestBody("audio/*".toMediaTypeOrNull())
                val fileName = "record_${System.currentTimeMillis()}.m4a"
                val part = MultipartBody.Part.createFormData("file", fileName, body)

                aiApi.requestStt(part)
            }.onSuccess { res ->
                // === 여기서 작업일지 내용에 STT 결과를 합친다 ===
                val old = etContent.text.toString()
                val merged = if (old.isBlank()) res.text else "$old\n${res.text}"
                etContent.setText(merged)
            }.onFailure { e ->
                Toast.makeText(
                    this@WorkLogActivity,
                    "STT 실패: ${e.message}",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    // 에뮬레이터용: 오디오 파일 선택
    private fun startAudioPicker() {
        val intent = Intent(Intent.ACTION_GET_CONTENT).apply {
            type = "audio/*"
        }
        startActivityForResult(intent, PICK_AUDIO)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == PICK_AUDIO && resultCode == RESULT_OK) {
            data?.data?.let { uploadForStt(it) }
        }
    }

    // ===================== 같은 구역 작업일지 =====================
    // equipmentId = 같은 구역을 대표하는 장비 id
    private fun loadSameAreaWorklogs() {
        scope.launch {
            runCatching {
                // 파라미터 없이 전체(or 같은 구역) 작업일지 조회
                workLogApi.list()
            }.onSuccess { list ->
                sameAreaAdapter.submitList(list)
            }.onFailure { e ->
                Toast.makeText(
                    this@WorkLogActivity,
                    "근처 작업일지 불러오기 실패: ${e.message}",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }
    // ===================== 공통 =====================
    private fun spinnerToLangCode(label: String?): String =
        when (label) {
            "한국어", "Korean" -> "ko"
            "영어", "English" -> "en"
            "베트남어", "Tiếng Việt" -> "vi"
            "태국어", "ไทย" -> "th"
            "중국어", "中文" -> "zh"
            else -> "ko"
        }

    override fun onDestroy() {
        super.onDestroy()
        scope.cancel()
    }
}