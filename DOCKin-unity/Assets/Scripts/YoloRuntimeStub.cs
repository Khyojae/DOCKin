using System.Collections.Generic;
using UnityEngine;

// YoloDetectManager에서 사용할 "모델 실행기" 껍데기
public class YoloRuntimeStub : MonoBehaviour
{
    // 나중에 실제로는 Texture2D 프레임을 받아서
    // YOLO ONNX/TFLite 추론을 돌릴 예정
    public List<DetectionResult> Run(Texture2D frame)
{
    var results = new List<DetectionResult>();

    // frame이 null이어도 그냥 더미 리턴 (에디터/테스트용)
    results.Add(new DetectionResult
    {
        label = "EQ-CRANE-1004",
        score = 0.95f,
        rect = new Rect(0.3f, 0.3f, 0.4f, 0.4f)
    });

    Debug.Log("[YoloRuntimeStub] Run() called, returning 1 dummy detection");
    return results;
}
}