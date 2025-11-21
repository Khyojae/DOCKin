using System.Collections.Generic;
using UnityEngine;
using UnityEngine.UI;
using UnityEngine.SceneManagement;
using Unity.Barracuda;   // Barracuda

[System.Serializable]
public class DetectionResult
{
    public string label;   // "box" or "forklift"
    public float score;    // 0.0 ~ 1.0
    public Rect rect;      // 화면 기준 0~1 정규화 박스 (x,y,w,h)
}

public class YoloDetectManager : MonoBehaviour
{
    [Header("YOLO Model")]
    public NNModel modelAsset;        // factory_yolo11n_416_best.onnx 연결
    public float confThreshold = 0.25f;
    public float iouThreshold = 0.45f;

    [Header("Test Input")]
    public Texture2D testTexture;     // 테스트용 이미지 1장 (416x416 아니어도 됨)

    [Header("UI")]
    public RawImage cameraView;       // testTexture 보여줄 용도
    public Text debugText;
    public Button detectButton;       // "탐지 테스트" 버튼
    public Button openMemoButton;     // "메모 열기" 버튼

    private Model runtimeModel;
    public YoloRuntimeOnnx runtime; 
    private IWorker worker;

    private List<DetectionResult> currentDetections = new List<DetectionResult>();
    private DetectionResult lastDetection = null;

    private const int INPUT_SIZE = 416;
    private readonly string[] classNames = { "box", "forklift" }; // class_id 0,1

    private void Awake()
    {
        // ONNX → Barracuda Model 로드
        if (modelAsset != null)
        {
            runtimeModel = ModelLoader.Load(modelAsset);
            worker = WorkerFactory.CreateWorker(WorkerFactory.Type.Auto, runtimeModel);
        }
        else
        {
            Debug.LogError("[YoloDetectManager] modelAsset 이 비어 있습니다.");
        }
    }

    private void Start()
    {
        if (debugText != null)
            debugText.text = "YOLO 대기 중...";

        if (detectButton != null)
            detectButton.onClick.AddListener(OnTestDetectClicked);

        if (openMemoButton != null)
        {
            openMemoButton.gameObject.SetActive(false);
            openMemoButton.onClick.AddListener(OnOpenMemoClicked);
        }

        // 에디터 테스트용으로 testTexture를 화면에 띄워두기
        if (cameraView != null && testTexture != null)
            cameraView.texture = testTexture;
    }

    private void OnDestroy()
    {
        worker?.Dispose();
    }

    // ================== 1) 테스트용 버튼 ==================

    // [탐지 테스트] 버튼에 연결
   public void OnTestDetectClicked()
{
    debugText.text = "추론 중...";

    if (runtime == null || testTexture == null)
    {
        debugText.text = "runtime 또는 testTexture 가 비어 있습니다.";
        return;
    }

    // Barracuda YOLO 실행
    var dets = runtime.Run(testTexture, confThreshold, iouThreshold);

    currentDetections.Clear();
    lastDetection = null;

    if (dets.Count == 0)
    {
        debugText.text = "탐지 결과 없음.";
        openMemoButton.gameObject.SetActive(false);
        return;
    }

    // 그냥 첫 번째 결과를 사용 (나중에 UI로 선택 가능)
    lastDetection = dets[0];
    currentDetections.AddRange(dets);

    debugText.text = $"탐지됨: {lastDetection.label} score={lastDetection.score:F2}";

    openMemoButton.gameObject.SetActive(true);
}

    // ================== 2) 메모 씬 이동 ==================

    private void OnOpenMemoClicked()
    {
        if (lastDetection == null)
        {
            debugText.text = "탐지 결과가 없습니다. 먼저 탐지 버튼을 눌러주세요.";
            return;
        }

        // 여기서는 forklift만 장비로 사용했다고 가정 (예: equipmentId = 1004)
        long equipmentId = 1004;

        var entry = AndroidEntryBridge.Instance;
        if (entry != null)
        {
            // routeJson은 지금은 필요 없으니 null
            entry.SetEntryData(equipmentId, "ko", null);
            Debug.Log($"[YoloDetectManager] SetEntryData equipmentId={equipmentId}, lang=ko");
        }
        else
        {
            Debug.LogWarning("[YoloDetectManager] AndroidEntryBridge.Instance 가 없습니다.");
        }

        SceneManager.LoadScene("ARMemoScene");
    }

    // ================== 3) YOLO 실행 / 후처리 ==================

    private List<DetectionResult> RunYoloOnTexture(Texture2D source)
    {
        // 1. 416x416으로 리사이즈
        Texture2D resized = ResizeToSquare(source, INPUT_SIZE);

        // 2. float[1,3,416,416] 전처리 (0~1 스케일, NCHW)
        float[] inputData = Preprocess(resized);

        using (var inputTensor = new Tensor(1, 3, INPUT_SIZE, INPUT_SIZE, inputData))
        {
            worker.Execute(inputTensor);
        }

        // 3. 출력 텐서 가져오기 (1,6,3549)
        Tensor outputTensor = worker.PeekOutput();
        float[] outputData = outputTensor.ToReadOnlyArray();
        outputTensor.Dispose();

        // 4. 후처리 → DetectionResult 리스트
        var detections = Postprocess(outputData, confThreshold, iouThreshold,
                                     INPUT_SIZE, INPUT_SIZE);

        // 5. 정규화된 Rect로 변환 (0~1)
        foreach (var det in detections)
        {
            float x = det.rect.x      / INPUT_SIZE;
            float y = det.rect.y      / INPUT_SIZE;
            float w = det.rect.width  / INPUT_SIZE;
            float h = det.rect.height / INPUT_SIZE;
            det.rect = new Rect(x, y, w, h);
        }

        return detections;
    }

    // 3-1. Texture 리사이즈
    private Texture2D ResizeToSquare(Texture2D src, int size)
    {
        if (src.width == size && src.height == size)
            return src;

        RenderTexture rt = RenderTexture.GetTemporary(size, size, 0);
        Graphics.Blit(src, rt);

        RenderTexture prev = RenderTexture.active;
        RenderTexture.active = rt;

        Texture2D tex = new Texture2D(size, size, TextureFormat.RGBA32, false);
        tex.ReadPixels(new Rect(0, 0, size, size), 0, 0);
        tex.Apply();

        RenderTexture.active = prev;
        RenderTexture.ReleaseTemporary(rt);
        return tex;
    }

    // 3-2. NCHW float 배열 만들기
    private float[] Preprocess(Texture2D tex)
    {
        Color32[] pixels = tex.GetPixels32();
        int imageSize = INPUT_SIZE * INPUT_SIZE;
        float[] data = new float[3 * imageSize];  // (3, H, W)

        for (int y = 0; y < INPUT_SIZE; y++)
        {
            for (int x = 0; x < INPUT_SIZE; x++)
            {
                int pixelIndex = y * INPUT_SIZE + x;
                Color32 c = pixels[pixelIndex];

                // 채널 순서: [0]=R, [1]=G, [2]=B
                data[0 * imageSize + pixelIndex] = c.r / 255f;
                data[1 * imageSize + pixelIndex] = c.g / 255f;
                data[2 * imageSize + pixelIndex] = c.b / 255f;
            }
        }
        return data;
    }

    // 3-3. YOLO 출력 후처리 (conf + NMS)
    private List<DetectionResult> Postprocess(
        float[] output,
        float confTh,
        float iouTh,
        int imgW,
        int imgH)
    {
        var candidates = new List<DetectionResult>();

        int channels = 6; // [cx,cy,w,h,score_box,score_forklift]
        int numPreds = output.Length / channels;

        for (int i = 0; i < numPreds; i++)
        {
            int offset = i * channels;

            float cx = output[offset + 0] * imgW;
            float cy = output[offset + 1] * imgH;
            float w  = output[offset + 2] * imgW;
            float h  = output[offset + 3] * imgH;

            float scoreBox      = output[offset + 4];
            float scoreForklift = output[offset + 5];

            int classId;
            float score;

            if (scoreBox > scoreForklift)
            {
                classId = 0;
                score = scoreBox;
            }
            else
            {
                classId = 1;
                score = scoreForklift;
            }

            if (score < confTh)
                continue;

            // 중심 좌표 → x1,y1,x2,y2
            float x1 = cx - w / 2f;
            float y1 = cy - h / 2f;
            float x2 = cx + w / 2f;
            float y2 = cy + h / 2f;

            Rect rect = new Rect(x1, y1, x2 - x1, y2 - y1);

            var det = new DetectionResult
            {
                label = classNames[classId],
                score = score,
                rect  = rect
            };
            candidates.Add(det);
        }

        // NMS
        candidates.Sort((a, b) => b.score.CompareTo(a.score));
        var results = new List<DetectionResult>();

        for (int i = 0; i < candidates.Count; i++)
        {
            var a = candidates[i];
            bool keep = true;

            for (int j = 0; j < results.Count; j++)
            {
                var b = results[j];
                if (IoU(a.rect, b.rect) > iouTh)
                {
                    keep = false;
                    break;
                }
            }

            if (keep)
                results.Add(a);
        }

        return results;
    }

    private float IoU(Rect a, Rect b)
    {
        float x1 = Mathf.Max(a.xMin, b.xMin);
        float y1 = Mathf.Max(a.yMin, b.yMin);
        float x2 = Mathf.Min(a.xMax, b.xMax);
        float y2 = Mathf.Min(a.yMax, b.yMax);

        float interW = Mathf.Max(0, x2 - x1);
        float interH = Mathf.Max(0, y2 - y1);
        float interArea = interW * interH;

        float unionArea = a.width * a.height + b.width * b.height - interArea;
        if (unionArea <= 0) return 0f;

        return interArea / unionArea;
    }
}