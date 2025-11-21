using UnityEngine;

public static class UnityToAndroidBridge
{
    // ArNavManager에서 호출하는 메서드
    public static void FinishWithResult(string jsonResult)
    {
#if UNITY_ANDROID && !UNITY_EDITOR
        try
        {
            using (AndroidJavaClass unityPlayer = 
                   new AndroidJavaClass("com.unity3d.player.UnityPlayer"))
            {
                AndroidJavaObject activity = unityPlayer.GetStatic<AndroidJavaObject>("currentActivity");
                activity.Call("finishWithResult", jsonResult);
            }
        }
        catch (System.Exception e)
        {
            Debug.LogError("[UnityToAndroidBridge] Error: " + e.Message);
        }
#else
        // 에디터에서는 그냥 로그만
        Debug.Log("[UnityToAndroidBridge] FinishWithResult called: " + jsonResult);
#endif
    }
}