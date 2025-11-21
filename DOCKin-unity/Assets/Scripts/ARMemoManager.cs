using System.Collections.Generic;
using UnityEngine;
using UnityEngine.UI;
using UnityEngine.XR.ARFoundation;
using UnityEngine.XR.ARSubsystems;

public class ARMemoManager : MonoBehaviour
{
    public ARRaycastManager raycastManager;
    public GameObject memoPrefab;
    public Text infoText;
    public Button saveButton;
    public Button closeButton;

    private Vector3? pendingPosition = null;
    private GameObject previewMemo;

    private void Start()
    {
        var entry = AndroidEntryBridge.Instance;
        infoText.text = $"Equipment: {entry.EquipmentId}, Lang: {entry.UserLang}";

        saveButton.onClick.AddListener(OnSaveMemoClicked);
        closeButton.onClick.AddListener(OnCloseClicked);
    }

    private void Update()
    {
        if (Input.touchCount == 0)
            return;

        Touch touch = Input.GetTouch(0);
        if (touch.phase == TouchPhase.Began)
        {
            TrySetPendingPosition(touch.position);
        }
    }

    private void TrySetPendingPosition(Vector2 screenPos)
    {
        var hits = new List<ARRaycastHit>();
        if (raycastManager.Raycast(screenPos, hits, TrackableType.PlaneWithinPolygon))
        {
            Pose hitPose = hits[0].pose;
            pendingPosition = hitPose.position;

            if (previewMemo == null)
            {
                previewMemo = Instantiate(memoPrefab, hitPose.position, hitPose.rotation);
            }
            else
            {
                previewMemo.transform.SetPositionAndRotation(hitPose.position, hitPose.rotation);
            }

            infoText.text = $"메모 위치 선택됨: {hitPose.position}";
        }
    }

    private void OnSaveMemoClicked()
    {
        if (pendingPosition == null)
        {
            infoText.text = "먼저 화면을 탭해서 메모 위치를 선택하세요.";
            return;
        }

        // 실제 메모 오브젝트 하나 생성 (preview 그대로 둬도 됨)
        var pos = pendingPosition.Value;
        Instantiate(memoPrefab, pos, Quaternion.identity);

        // TODO: 나중에 Kotlin으로 "이 위치에 메모 생성해줘" 요청 (equipmentId + pos)
        Debug.Log($"[ARMemoManager] Memo saved at {pos}");

        infoText.text = $"메모 저장 완료: {pos}";
    }

    private void OnCloseClicked()
    {
        // TODO: 나중에 Kotlin에 "메모 씬 종료" 알릴 때 ActivityResult or 콜백 사용
        UnityToAndroidBridge.FinishWithResult("{\"status\":\"closed\"}");
    }
}