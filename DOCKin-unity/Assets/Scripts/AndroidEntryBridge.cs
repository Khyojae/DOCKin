using UnityEngine;

public class AndroidEntryBridge : MonoBehaviour
{
    public static AndroidEntryBridge Instance { get; private set; }

    // Kotlin에서 넘겨줄 값들 (읽기 전용)
    public long   EquipmentId     { get; private set; }
    public string UserLang        { get; private set; }
    public string RouteConfigJson { get; private set; }

    private void Awake()
    {
        // 싱글톤 세팅
        if (Instance != null && Instance != this)
        {
            Destroy(gameObject);
            return;
        }

        Instance = this;
        DontDestroyOnLoad(gameObject);
    }

    // ★ YoloDetectManager / Kotlin에서 호출하는 세터 메서드
    public void SetEntryData(long equipmentId, string userLang, string routeJson)
    {
        EquipmentId     = equipmentId;
        UserLang        = userLang;
        RouteConfigJson = routeJson;

        Debug.Log($"[AndroidEntryBridge] SetEntryData id={EquipmentId}, lang={UserLang}, routeJson={routeJson}");
    }
}