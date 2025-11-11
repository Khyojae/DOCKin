from fastapi import APIRouter
from ..core.config import settings

router = APIRouter()


@router.get("/health")
def health():
    return {"status": "ok", "app": settings.app_name, "env": settings.app_env}


@router.get("/ready")
def ready():
    return {"ready": True}
