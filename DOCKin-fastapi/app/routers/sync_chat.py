# /v1/chat
from fastapi import APIRouter, Depends, HTTPException, status
from ..schemas.chat import ChatRequest, ChatResponse
from ..core.auth import require_service_token
from ..core.config import settings

router = APIRouter(tags=["chat"])


@router.post(
    "/api/chat",
    response_model=ChatResponse,
    dependencies=[Depends(require_service_token)],
)
async def chat(req: ChatRequest):
    # 모의 응답 분기
    if not settings.openai_enabled:
        # 마지막 user 메시지 추출
        last_user = next(
            (m.content for m in reversed(req.messages) if m.role == "user"), ""
        )
        # 모의 응답 생성
        reply = f"[mock {req.domain}/{req.lang}] {last_user}"
        return ChatResponse(reply=reply, model="mock", traceId=req.traceId)
    # 실제 모델 경로는 추후 연결
    raise HTTPException(
        status_code=status.HTTP_501_NOT_IMPLEMENTED,
        detail="chat_provider_not_configured",
    )
