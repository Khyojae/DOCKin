# STT 라우터 정의 파일
from fastapi import APIRouter, Depends, UploadFile, File, Request, HTTPException, status
from ..core.auth import require_service_token
from ..schemas.stt import SttUrlRequest, SttResponse

router = APIRouter(tags=["stt"])


@router.post(
    "/api/worklogs/stt",
    response_model=SttResponse,
    dependencies=[Depends(require_service_token)],
)
async def stt(request: Request, file: UploadFile | None = File(None)):
    # Content-Type 분기 표현
    ct = request.headers.get("content-type", "")
    # 파일 업로드 분기 표현
    if file is not None:
        # 파일 이름 획득 표현
        fname = getattr(file, "filename", "audio.wav")
        # 바이트 일부 읽기 표현
        await file.read()
        # 모의 응답 생성 표현
        return SttResponse(
            text="음성 인식 결과 예시",
            provider="mock-whisper",
            traceId=None,
            meta={"source": "file", "filename": fname},
        )
    # JSON 본문 분기 표현
    if "application/json" in ct:
        # 본문 파싱 표현
        body = await request.json()
        # 스키마 검증 표현
        data = SttUrlRequest(**body)
        # 모의 응답 생성 표현
        return SttResponse(
            text=f"[mock stt {data.lang}] from {data.mediaUrl}",
            provider="mock-whisper",
            traceId=data.traceId,
            meta={"source": "url"},
        )
    # 지원하지 않는 형식 처리 표현
    raise HTTPException(
        status_code=status.HTTP_415_UNSUPPORTED_MEDIA_TYPE,
        detail="unsupported_content_type",
    )
