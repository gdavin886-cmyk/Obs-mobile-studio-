# Release v2.0.0

## 🚀 Major Updates & Features
* **Real RTMP Network Verification**: Replaced simulated connection tasks with real TCP socket network connections to actively verify Server URLs (port 443 for RTMPS, 1935 for RTMP) and Stream Keys before broadcasting.
* **Streamlined Multi-Destination Streaming**: Parallelized connection checks across all configured broadcasting destinations (YouTube, Twitch, Facebook, Custom RTMP, Telegram).

## 🛠️ Comprehensive Error Fixes & Stability Improvements
* **MESA Rendernode Crash Resolved**: Fixed critical virtual device rendering crashes by explicitly forcing software layer rendering (`LAYER_TYPE_SOFTWARE`) across all `WebView`, `VideoView`, and Camera `PreviewView` components.
* **CameraX Timeout Exception Handled**: Added robust try-catch wrapping around `ProcessCameraProvider` future retrieval and lifecycle binding to gracefully handle emulators lacking physical camera hardware without crashing.
* **Chromium Cache Error Resilience**: Handled file enumerator and cache index initialization errors gracefully to ensure smooth web and streaming source views.
* **Clean Authentication & Git Sync**: Streamlined repository tracking and clean release pushing with token authentication.
