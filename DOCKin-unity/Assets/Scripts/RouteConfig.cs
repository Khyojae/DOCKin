using UnityEngine;

[System.Serializable]
public class RoutePoint
{
    public float x;
    public float z;
}

[System.Serializable]
public class RouteConfig
{
    public int buildingId;
    public int floor;

    [System.Serializable]
    public class TargetInfo
    {
        public long equipmentId;
    }

    [System.Serializable]
    public class StartInfo
    {
        public float x;
        public float z;
    }

    public TargetInfo target;
    public StartInfo start;

    public RoutePoint[] waypoints;
}