using UnityEngine;

public class ArNavManager : MonoBehaviour
{
    public GameObject arrowPrefab;       // AR 화살표 프리팹
    public float threshold = 0.7f;       // THRESHOLD: 도착 판정 거리 (m)
    public float yOffset = 0.0f;         // 화살표가 바닥보다 약간 위에 뜨게 할 때 사용

    private RouteConfig route;           // waypoints 포함된 경로 정보
    private int currentIndex = 0;        // 현재 목표 waypoint 인덱스
    private Transform arCamera;          // 현재 Pose (카메라)
    private GameObject arrowInstance;    // 실제 화살표 오브젝트

    private float smallInterval = 0.05f; // SMALL_INTERVAL (Update 주기 비슷하게)

    private void Start()
    {
        // 1. 작업 ID에 해당하는 경로(waypoint 리스트) 로딩
        LoadRouteFromJson(AndroidEntryBridge.Instance.RouteConfigJson);

        // AR 카메라 참조
        var camObj = GameObject.FindWithTag("MainCamera");
        if (camObj != null)
            arCamera = camObj.transform;

        // 2. 현재 위치 기준, 가장 가까운 시작 waypoint 선택
        currentIndex = FindNearestWaypointIndex(GetCurrentPose(), route.waypoints);

        // 3. 첫 waypoint 방향으로 AR 화살표 생성
        SpawnArrow(route.waypoints[currentIndex]);

        // 4. 네비게이션 루프 시작
        //    (Unity에서는 Update()가 while-loop + wait 역할을 함)
    }

    private void Update()
    {
        if (route == null || arCamera == null || route.waypoints == null || route.waypoints.Length == 0)
            return;

        // while not reachedFinal(waypoints, currentIndex):
        if (currentIndex >= route.waypoints.Length)
            return;

        // 4.1. 현재 pose / 현재 waypoint 거리 계산
        Vector3 currentPose = GetCurrentPose();
        Vector3 targetPos = ToWorldPosition(route.waypoints[currentIndex]);
        float dist = Vector3.Distance(currentPose, targetPos);

        // 4.2. 충분히 가까워지면 다음 waypoint로 넘기기
        if (dist < threshold)
        {
            currentIndex++;

            if (currentIndex < route.waypoints.Length)
            {
                // 다음 waypoint를 향하도록 화살표 타깃 갱신
                UpdateArrowTarget(route.waypoints[currentIndex]);
            }
            else
            {
                // 최종 도착
                ShowArrivedMessage();
                // TODO: 나중에 Kotlin 콜백 (finishWithResult) 호출 위치
                UnityToAndroidBridge.FinishWithResult("{\"status\":\"arrived\"}");
                return;
            }
        }

        // 4.3. 사용자가 바라보는 방향에 맞게 화살표 회전/정렬
        UpdateArrowOrientation(currentPose, targetPos);

        // wait(SMALL_INTERVAL) 역할 → Update가 프레임 단위로 호출되므로 별도 코루틴은 생략
    }

    // ---------- 슬라이드에 나온 함수들 대응 구현 ----------

    // getCurrentArPose()
    private Vector3 GetCurrentPose()
    {
        if (arCamera == null) return Vector3.zero;
        Vector3 p = arCamera.position;
        p.y = 0f; // 바닥 평면 기준으로만 거리 계산
        return p;
    }

    // loadWaypointsFromServer(taskId)에 해당 (지금은 JSON → RouteConfig)
    private void LoadRouteFromJson(string json)
    {
        if (string.IsNullOrEmpty(json))
        {
            // 에디터 테스트용 기본 JSON
            json = @"{
                ""buildingId"":1,
                ""floor"":3,
                ""target"":{""equipmentId"":1004},
                ""start"":{""x"":0.0, ""z"":0.0},
                ""waypoints"":[
                    {""x"":0.0,""z"":0.0},
                    {""x"":0.0,""z"":2.0},
                    {""x"":2.0,""z"":4.0}
                ]
            }";
        }

        route = JsonUtility.FromJson<RouteConfig>(json);
    }

    // findNearestWaypointIndex(waypoints, currentPose)
    private int FindNearestWaypointIndex(Vector3 currentPose, RoutePoint[] waypoints)
    {
        if (waypoints == null || waypoints.Length == 0) return 0;

        float minDist = float.MaxValue;
        int nearest = 0;
        for (int i = 0; i < waypoints.Length; i++)
        {
            Vector3 wp = ToWorldPosition(waypoints[i]);
            float d = Vector3.Distance(currentPose, wp);
            if (d < minDist)
            {
                minDist = d;
                nearest = i;
            }
        }
        return nearest;
    }

    // spawnArrow(waypoints[currentIndex].position)
    private void SpawnArrow(RoutePoint point)
    {
        if (arrowPrefab == null) return;
        Vector3 pos = ToWorldPosition(point);
        arrowInstance = Instantiate(arrowPrefab, pos, Quaternion.identity);
    }

    // updateArrowTarget(waypoints[currentIndex].position)
    private void UpdateArrowTarget(RoutePoint point)
    {
        if (arrowInstance == null) return;
        Vector3 pos = ToWorldPosition(point);
        arrowInstance.transform.position = pos;
    }

    // updateArrowOrientation(currentPose.position, waypoints[currentIndex].position)
    private void UpdateArrowOrientation(Vector3 currentPose, Vector3 targetPos)
    {
        if (arrowInstance == null) return;

        Vector3 dir = targetPos - currentPose;
        dir.y = 0f;
        if (dir.sqrMagnitude < 0.0001f) return;

        Quaternion lookRot = Quaternion.LookRotation(dir.normalized, Vector3.up);
        arrowInstance.transform.rotation = lookRot;
    }

    private Vector3 ToWorldPosition(RoutePoint p)
    {
        return new Vector3(p.x, yOffset, p.z);
    }

    private void ShowArrivedMessage()
    {
        Debug.Log("[ArNavManager] Arrived at final waypoint.");
        // 나중에 UI 텍스트/팝업으로 "목적지에 도착했습니다" 같은 메시지 띄우면 됨
    }
}