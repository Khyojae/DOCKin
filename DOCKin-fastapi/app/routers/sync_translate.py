# /v1/translate
from fastapi import APIRouter, Depends, HTTPException, status
from ..schemas.translate import TranslateRequest, TranslateResponse
from ..core.auth import require_service_token
from ..core.config import settings

router = APIRouter(tags=["translate"])


@router.post(
    "/api/translate",
    response_model=TranslateResponse,
    dependencies=[Depends(require_service_token)],
)
async def translate(req: TranslateRequest):
    # 모의 응답 분기
    if not settings.translate_enabled:
        mock = f"[mock translate {req.source}->{req.target}] {req.text}"
        return TranslateResponse(
            translated=mock, model="mock-nllb", traceId=req.traceId
        )
    # 실제 번역 경로는 추후 연결
    raise HTTPException(
        status_code=status.HTTP_501_NOT_IMPLEMENTED,
        detail="translate_provider_not_configured",
    )
