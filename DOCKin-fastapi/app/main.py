from fastapi import FastAPI
from fastapi.middleware.cors import CORSMiddleware
from .routers import health, sync_chat, sync_translate, stt

app = FastAPI(title="fastapi-ai")

app.add_middleware(
    CORSMiddleware,
    allow_origins=["*"],
    allow_credentials=False,
    allow_methods=["*"],
    allow_headers=["*"],
)

app.include_router(health.router)
app.include_router(sync_chat.router)
app.include_router(sync_translate.router)
app.include_router(stt.router)


@app.get("/")
def root():
    return {"hello": "fastapi-ai"}
