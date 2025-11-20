from ultralytics import YOLO

model = YOLO("/Users/emfpdlzj/Desktop/DOCKin-yolo/dataset/runs/detect/train/weights/best.pt")  # yolov8n으로 학습 완료된 가중치

model.export(
    format="onnx",   # ONNX 포맷
    opset=11,        # Barracuda 호환을 위해 11 고정
    dynamic=False,   # 동적 shape 비활성화 (고정 입력 크기)
    simplify=True,   # 그래프 단순화
    imgsz=416        # 학습 시 사용한 입력 크기와 동일
)