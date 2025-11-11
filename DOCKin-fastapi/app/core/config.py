from pydantic_settings import BaseSettings

class Settings(BaseSettings):
    app_name: str = "fastapi-ai"
    app_env: str = "local"
    app_port: int = 8000
    class Config:
        env_file = ".env"

settings = Settings()