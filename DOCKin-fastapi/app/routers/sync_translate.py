# /v1/translate 동기 엔드포인트
# 서비스 토큰 인증 적용
# TRANSLATE_ENABLED=false 이면 모의 응답 반환

from fastapi import APIRouter, Depends, HTTPException, status
from ..schemas.translate import TranslateRequest, TranslateResponse
from ..core.auth import require_service_token
from ..core.config import settings
from ..services.translate_nllb import translate_once

router = APIRouter(tags=["translate"])


@router.post(
    "/v1/translate",
    response_model=TranslateResponse,
    dependencies=[Depends(require_service_token)],
)
async def translate(req: TranslateRequest):
    # 모의 응답 경로
    if not settings.translate_enabled:
        # 간단한 모의 응답 구성
        # 실제 번역 대신 입력과 방향만 표시
        mock = f"[mock translate {req.source}->{req.target}] {req.text}"
        return TranslateResponse(
            translated=mock, model="mock-nllb", traceId=req.traceId
        )
    # 실제 공급자 경로
    try:
        translated, provider = translate_once(req.model_dump())
        return TranslateResponse(
            translated=translated, model=provider, traceId=req.traceId
        )
    except NotImplementedError as e:
        # 공급자 미구성 에러
        raise HTTPException(status_code=status.HTTP_501_NOT_IMPLEMENTED, detail=str(e))
    except Exception:
        # 일반 공급자 오류
        raise HTTPException(
            status_code=status.HTTP_502_BAD_GATEWAY, detail="translate_provider_error"
        )
