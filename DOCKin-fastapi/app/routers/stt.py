from fastapi import APIRouter, UploadFile, File
from app.schemas.stt import SttResponse
from faster_whisper import WhisperModel
import tempfile

router = APIRouter()

model = WhisperModel("tiny", device="cpu")


@router.post("/api/worklogs/stt", response_model=SttResponse)
async def stt(file: UploadFile = File(None), mediaUrl: str | None = None):

    # 1) 파일 업로드 기반 (kotlin 앱에서 녹음 → multipart 업로드)
    if file:
        with tempfile.NamedTemporaryFile(delete=False, suffix=".wav") as tmp:
            tmp.write(await file.read())
            audio_path = tmp.name

        segments, info = model.transcribe(audio_path, beam_size=1, language="ko")
        text = "".join([seg.text for seg in segments])

        return SttResponse(text=text, provider="whisper-faster")

    # 2) URL 기반 (스프링 → fastapi URL전달)
    if mediaUrl:
        # 필요하면 httpx로 다운로드 구현
        return SttResponse(text="[미구현] URL 기반 STT 필요시 추가", provider="whisper")

    return SttResponse(text="", provider="whisper")
