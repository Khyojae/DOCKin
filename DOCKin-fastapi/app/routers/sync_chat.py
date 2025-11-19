from fastapi import APIRouter
from openai import OpenAI
from app.schemas.chat import ChatRequest, ChatResponse
from app.core.config import settings

router = APIRouter()
client = OpenAI(api_key=settings.openai_api_key)


@router.post("/api/chat", response_model=ChatResponse)
async def chat(req: ChatRequest):
    # OpenAI Chat Completion 호출
    completion = client.chat.completions.create(
        model=settings.openai_model,
        messages=[m.model_dump() for m in req.messages],
    )

    # 첫 번째 답변만 사용
    reply = completion.choices[0].message.content

    # 통일된 응답 스키마로 반환
    return ChatResponse(
        reply=reply,
        model=settings.openai_model,
        traceId=req.traceId,
    )
