from fastapi import APIRouter, HTTPException
from app.schemas.translate import TranslateRequest, TranslateResponse
from transformers import AutoTokenizer, AutoModelForSeq2SeqLM

router = APIRouter()

# NLLB 경량 모델 이름 정의.
MODEL_NAME = "facebook/nllb-200-distilled-600M"

# 토크나이저와 모델을 서버 시작 시 한 번만 로드.
tokenizer = AutoTokenizer.from_pretrained(MODEL_NAME)
model = AutoModelForSeq2SeqLM.from_pretrained(MODEL_NAME)

# 외부에서 들어오는 언어 코드와 NLLB 내부 언어 코드를 매핑.
LANG_CODE_MAP: dict[str, str] = {
    "ko": "kor_Hang",
    "en": "eng_Latn",
    "vi": "vie_Latn",
    "zh": "zho_Hans",
    "th": "tha_Thai",
}


@router.post("/api/translate", response_model=TranslateResponse)
async def translate(req: TranslateRequest):
    # 지원하지 않는 언어 코드가 들어오면 400 에러 반환.
    if req.source not in LANG_CODE_MAP or req.target not in LANG_CODE_MAP:
        raise HTTPException(
            status_code=400,
            detail=f"지원하지 않는 번역 언어 코드. source={req.source}, target={req.target}",
        )

    # NLLB 언어 코드 설정.
    src_lang = LANG_CODE_MAP[req.source]
    tgt_lang = LANG_CODE_MAP[req.target]

    # 토크나이저에 소스 언어 설정.
    tokenizer.src_lang = src_lang

    # 입력 문장을 토크나이징.
    inputs = tokenizer(
        req.text,
        return_tensors="pt",
        padding=True,
        truncation=True,
        max_length=256,
    )

    # NLLB 모델로 번역 토큰 생성.
    generated = model.generate(
        **inputs,
        max_length=256,
        num_beams=4,
        early_stopping=True,
        forced_bos_token_id=tokenizer.lang_code_to_id[tgt_lang],
    )

    # 토큰을 실제 번역 텍스트로 디코딩.
    translated = tokenizer.batch_decode(generated, skip_special_tokens=True)[0]

    # 공통 응답 스키마에 맞춰 반환.
    return TranslateResponse(
        translated=translated,
        model=MODEL_NAME,
        traceId=req.traceId,
    )
