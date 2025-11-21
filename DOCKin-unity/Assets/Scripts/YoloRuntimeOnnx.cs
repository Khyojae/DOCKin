using System.Collections.Generic;
using UnityEngine;
using Unity.Barracuda;

// DetectionResult는 이미 YoloDetectManager에 정의돼 있으니까
// 같은 네임스페이스 안이면 그대로 재사용됨.
// (다른 파일에 중복 정의만 안 하면 됨)

public class YoloRuntimeOnnx : MonoBehaviour
{
    [Header("Model")]
    public NNModel modelAsset;          // best (NN Model) 드래그할 자리
    public int inputSize = 416;         // 416 x 416

    private Model _model;
    private IWorker _worker;

    private void Awake()
    {
        if (modelAsset == null)
        {
            Debug.LogError("[YoloRuntimeOnnx] modelAsset 이 비어 있습니다.");
            return;
        }

        _model = ModelLoader.Load(modelAsset);
        // Auto로 두면 CPU/GPU 알아서 선택
        _worker = WorkerFactory.CreateWorker(WorkerFactory.Type.Auto, _model);
        Debug.Log("[YoloRuntimeOnnx] Model loaded.");
    }

    private void OnDestroy()
    {
        _worker?.Dispose();
    }

    // ---------------------------
    // 메인 엔트리: Texture2D → Detect
    // ---------------------------
    public List<DetectionResult> Run(Texture2D source, float confTh, float iouTh)
    {
        var results = new List<DetectionResult>();

        if (_worker == null || source == null)
        {
            Debug.LogWarning("[YoloRuntimeOnnx] worker 또는 source 가 null");
            return results;
        }

        // 1) 416x416으로 리사이즈
        Texture2D resized = ResizeTexture(source, inputSize, inputSize);

        // 2) 0~255 → 0~1 float32, CHW 배열 만들기
        float[] inputData = MakeInputCHW(resized);

        // 3) Tensor 생성 (1, 3, 416, 416)
        var shape = new TensorShape(1, 3, inputSize, inputSize);
        using var input = new Tensor(shape, inputData);

        // 4) 추론
        _worker.Execute(input);
        using Tensor output = _worker.PeekOutput();

        // 디버깅용으로 shape 한 번 찍어보기
        Debug.Log($"[YoloRuntimeOnnx] output shape = {output.shape}");

        // 5) 후처리 (output: 1 x 6 x 3549 가정)
        // Barracuda는 보통 (N, C, H, W) 또는 (N, H, W, C) 로 펼쳐짐.
        // 여기서는 "채널=6, 나머지 축 곱=3549" 인 케이스만 처리.

        var s = output.shape;
        int numPred = 0;
        bool channelsFirst = false;

        if (s.channels == 6)
        {
            // (N, H, W, C=6) 형식
            numPred = s.height * s.width;
            channelsFirst = false; // 마지막 차원이 채널
        }
        else if (s.height == 6)
        {
            // (N, C=?, H=6, W=?), 혹은 (N, H=6, W=?, C=?)
            // 여기선 단순히 "height=6" 인 경우로 처리
            numPred = s.width * s.channels;
            channelsFirst = true;  // height가 채널 역할
        }
        else
        {
            Debug.LogWarning($"[YoloRuntimeOnnx] 예상과 다른 shape: {s}");
            return results;
        }

        float imgW = inputSize;
        float imgH = inputSize;

        float confThreshold = confTh;
        float iouThreshold = iouTh;

        var candidates = new List<DetectionResult>();

        for (int i = 0; i < numPred; i++)
        {
            float cx, cy, w, h, score0, score1;

            if (!channelsFirst)
            {
                // (N, H, W, C=6)
                int y = i / s.width;
                int x = i % s.width;

                cx     = output[0, y, x, 0];
                cy     = output[0, y, x, 1];
                w      = output[0, y, x, 2];
                h      = output[0, y, x, 3];
                score0 = output[0, y, x, 4];
                score1 = output[0, y, x, 5];
            }
            else
            {
                // (N, C=?, H=6, W=? ) → height=6
                int c = i / s.width;
                int x = i % s.width;

                cx     = output[0, 0, c, x];
                cy     = output[0, 1, c, x];
                w      = output[0, 2, c, x];
                h      = output[0, 3, c, x];
                score0 = output[0, 4, c, x];
                score1 = output[0, 5, c, x];
            }

            // 두 클래스 중 큰 쪽을 score로
            int classId = score0 > score1 ? 0 : 1;
            float score = Mathf.Max(score0, score1);
            if (score < confThreshold) continue;

            // 0~1 → 416 기준 픽셀
            float bw = w * imgW;
            float bh = h * imgH;
            float bx = (cx * imgW) - bw / 2f;
            float by = (cy * imgH) - bh / 2f;

            var rect = new Rect(bx, by, bw, bh);

            string label = classId == 0 ? "box" : "forklift";

            candidates.Add(new DetectionResult
            {
                label = label,
                score = score,
                rect  = rect
            });
        }

        // 6) NMS (간단 버전, 클래스 공통 NMS)
        results = Nms(candidates, iouThreshold);

        Debug.Log($"[YoloRuntimeOnnx] {results.Count} boxes after NMS");
        return results;
    }

    // ----------------- 헬퍼 함수들 -----------------

    private Texture2D ResizeTexture(Texture2D src, int width, int height)
    {
        RenderTexture rt = RenderTexture.GetTemporary(width, height, 0);
        Graphics.Blit(src, rt);

        RenderTexture prev = RenderTexture.active;
        RenderTexture.active = rt;
        Texture2D tex = new Texture2D(width, height, TextureFormat.RGBA32, false);
        tex.ReadPixels(new Rect(0, 0, width, height), 0, 0);
        tex.Apply();

        RenderTexture.active = prev;
        RenderTexture.ReleaseTemporary(rt);
        return tex;
    }

    // Texture2D → float[3 * H * W] (CHW, 0~1)
    private float[] MakeInputCHW(Texture2D tex)
    {
        int w = tex.width;
        int h = tex.height;
        Color32[] pixels = tex.GetPixels32();
        float[] data = new float[3 * w * h];

        for (int i = 0; i < pixels.Length; i++)
        {
            var p = pixels[i];
            // 0~1 스케일
            float r = p.r / 255f;
            float g = p.g / 255f;
            float b = p.b / 255f;

            // CHW: [c * (H*W) + i]
            data[0 * w * h + i] = r;
            data[1 * w * h + i] = g;
            data[2 * w * h + i] = b;
        }

        return data;
    }

    // 매우 단순한 NMS 구현 (IoU 기준)
    private List<DetectionResult> Nms(List<DetectionResult> boxes, float iouTh)
    {
        var sorted = new List<DetectionResult>(boxes);
        sorted.Sort((a, b) => b.score.CompareTo(a.score));  // score 내림차순

        var result = new List<DetectionResult>();

        while (sorted.Count > 0)
        {
            var best = sorted[0];
            sorted.RemoveAt(0);
            result.Add(best);

            for (int i = sorted.Count - 1; i >= 0; i--)
            {
                if (IoU(best.rect, sorted[i].rect) > iouTh)
                    sorted.RemoveAt(i);
            }
        }

        return result;
    }

    private float IoU(Rect a, Rect b)
    {
        float x1 = Mathf.Max(a.xMin, b.xMin);
        float y1 = Mathf.Max(a.yMin, b.yMin);
        float x2 = Mathf.Min(a.xMax, b.xMax);
        float y2 = Mathf.Min(a.yMax, b.yMax);

        float inter = Mathf.Max(0, x2 - x1) * Mathf.Max(0, y2 - y1);
        float union = a.width * a.height + b.width * b.height - inter;
        if (union <= 0) return 0;
        return inter / union;
    }
}