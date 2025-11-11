# 번역 서비스 어댑터 골격
# NLLB 또는 대체 번역 공급자 호출을 이 모듈에서 처리

from typing import Dict, Tuple


def translate_once(payload: Dict) -> Tuple[str, str]:
    # payload 는 TranslateRequest.dict() 형식
    # 현재는 실제 번역 공급자 연동 없음
    # 나중에 NLLB 또는 OpenAI 번역으로 교체 예정
    raise NotImplementedError("translate provider not configured")
