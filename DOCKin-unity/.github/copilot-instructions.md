# Copilot / AI Agent Instructions for DOCKin-unity

Purpose: Help an AI coding agent become productive quickly in this Unity project by documenting the project's architecture, integration points, developer workflows, and code conventions that are discoverable in the repo.

- **Quick Start (developer workflow)**
  - Open the project in the Unity Editor (`File > Open Project` pointing at the repo root).
  - For Android testing the project relies on `AndroidEntryBridge` to read intent extras and `UnityToAndroidBridge` for callbacks. In the Editor these classes provide sane defaults so you can iterate without an Android device.
  - To build for Android use Unity's Build Settings (Editor). If you use the Unity CLI, supply your own `-executeMethod` build script; example placeholder:
    ```bash
    $UNITY_BIN -projectPath /path/to/DOCKin-unity -buildTarget Android -quit -batchmode -executeMethod MyBuild.PerformAndroidBuild
    ```

- **High-level architecture / responsibilities**
  - Unity front-end (AR scenes + UI) lives under `Assets/` and is component-based (MonoBehaviours).
  - Android <> Unity integration:
    - `Assets/Scripts/AndroidEntryBridge.cs`: singleton that reads Android Intent extras (equipment id, route JSON, lang). Uses `#if UNITY_ANDROID && !UNITY_EDITOR` to switch between real Android behavior and Editor defaults.
    - `Assets/Scripts/UnityToAndroidBridge.cs`: static helper to call Android activity methods and to send a result back with `FinishWithResult(string jsonResult)`.
  - AR & UX components:
    - `Assets/Scripts/ArNavManager.cs`: route-following logic. Reads a route JSON (mapped to `RouteConfig`), finds nearest waypoint, spawns/rotates an arrow prefab, and calls `UnityToAndroidBridge.FinishWithResult` on arrival.
    - `Assets/Scripts/ARMemoManager.cs`: AR placement helper using `ARRaycastManager` and prefab instantiation; provides Editor-friendly behavior.
    - `Assets/Scripts/YoloDetectManager.cs`: camera + periodic detection loop (coroutine) using `WebCamTexture` (currently stubbed/fake detection).
  - Data model for JSON route configuration: `Assets/Scripts/RouteConfig.cs` (`RouteConfig`, `RoutePoint`, `RouteTarget`). Unity's `JsonUtility` is used for JSON (so keep JSON shape flat and matching the serializable classes).

- **Important code patterns & conventions (project-specific)**
  - Platform guards: Use `#if UNITY_ANDROID && !UNITY_EDITOR` in bridge code. The Editor path uses default/test values — rely on these while iterating locally.
  - Singletons for cross-scene shared state: `AndroidEntryBridge.Instance` is `DontDestroyOnLoad` and expected to be present in scenes that need Android input.
  - JSON parsing: `JsonUtility.FromJson<RouteConfig>(json)` — JSON must match the exact field names in `RouteConfig` (lowerCamel style in this repo).
  - Camera / pose handling: `ArNavManager` uses `GameObject.FindWithTag("MainCamera")` to locate the AR camera and zeros the `y` component for ground-plane distance math.
  - Prefabs: AR visuals (arrow, memo) are instantiated from prefabs placed in scenes. Look under `Assets/Prefabs` and scene files for names.
  - Logging: code uses `Debug.Log` for informational output. Use the same for quick debugging messages.

- **Concrete examples (copyable)**
  - Sample route JSON (from `ArNavManager.LoadRouteFromJson`):
    ```json
    {
      "buildingId":1,
      "floor":3,
      "target":{"equipmentId":1004},
      "start":{"x":0.0, "z":0.0},
      "waypoints":[{"x":0.0,"z":0.0},{"x":0.0,"z":2.0},{"x":2.0,"z":4.0}]
    }
    ```
  - Call Kotlin method from Unity (example): `UnityToAndroidBridge.CallKotlin("yourMethod", arg1, arg2)`
  - Send navigation result back to Activity: `UnityToAndroidBridge.FinishWithResult("{\"status\":\"arrived\"}")`

- **Files to inspect when editing or extending features**
  - `Assets/Scripts/ArNavManager.cs` — navigation loop, waypoint handling, arrow spawn/orientation
  - `Assets/Scripts/RouteConfig.cs` — JSON->C# mapping for routes
  - `Assets/Scripts/AndroidEntryBridge.cs` — reads Android intent extras; provides Editor defaults
  - `Assets/Scripts/UnityToAndroidBridge.cs` — static bridge for calling Activity methods
  - `Assets/Scripts/ARMemoManager.cs` — AR placement, `ARRaycastManager` usage
  - `Assets/Scripts/YoloDetectManager.cs` — webcam + coroutine detection example

- **What NOT to assume / gotchas**
  - There are no project-level C# namespaces in use; new files should follow the same pattern unless refactoring intentionally.
  - Unity's `JsonUtility` is strict: do not expect flexible property mapping (no dictionaries or deep polymorphism without custom converters).
  - The project relies on tags (e.g. `MainCamera`) and serialized prefab fields — changes to scene objects or tag names can break code that uses `FindWithTag` or inspector-assigned prefabs.

If anything in this file is unclear or you want additional detail (build scripts, CI, or examples of Android/Kotlin integration), tell me which area to expand and I will iterate.
