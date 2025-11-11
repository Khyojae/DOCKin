from fastapi import FastAPI
from .routers import health

app = FastAPI(title="fastapi-ai")
app.include_router(health.router)


@app.get("/")
def root():
    return {"hello": "fastapi-ai"}
