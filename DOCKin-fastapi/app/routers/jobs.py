# /v1/ai/jobs
from fastapi import APIRouter, Depends
from ..core.auth import require_service_token
from ..schemas.job import JobCreate, JobAccepted
from ..workers.memqueue import enqueue

router = APIRouter(tags=["jobs"])


@router.post(
    "/api/ai/jobs",
    response_model=JobAccepted,
    dependencies=[Depends(require_service_token)],
)
async def create_job(req: JobCreate):
    # 큐에 작업 등록
    await enqueue(req.model_dump())
    # 즉시 접수 응답
    return JobAccepted(accepted=True, jobId=req.jobId)
