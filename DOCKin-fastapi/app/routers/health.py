from fastapi import APIRouter
from ..core.config import settings

router = APIRouter()


@router.get("/health")
def health():
    return {"status": "ok", "app": settings.app_name, "env": settings.app_env}


@router.get("/ready")
def ready():
    # 준비 상태와 주요 플래그 노출
    return {
        "ready": True,
        "openai_enabled": settings.openai_enabled,
        "chat_model": settings.chat_model,
        "translate_enabled": settings.translate_enabled,
    }
