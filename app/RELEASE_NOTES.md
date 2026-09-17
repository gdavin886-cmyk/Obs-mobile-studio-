# Release v2.1.0

## 🚀 Major Updates & Features
* **Media Projection Screen Broadcasting Permissions**: Added `FOREGROUND_SERVICE_MEDIA_PROJECTION` permissions and service type configuration to fully support Android screen casting and mobile live broadcast capture.
* **Real RTMP Network Verification**: Replaced simulated connection tasks with real TCP socket network connections to actively verify Server URLs and Stream Keys before broadcasting.

## 🛠️ Comprehensive Error Fixes & Stability Improvements
* **MESA Rendernode Resilience**: Enforced software rendering fallbacks across all `WebView`, `VideoView`, and Camera `PreviewView` components to ensure error-free rendering on virtual emulators.
* **CameraX Timeout Exception Handled**: Robustly handled camera initialization and binding timeouts to prevent crashes on headless or non-standard Android test environments.
* **Git Sync & Tagging**: Clean release tracking and synchronization with GitHub.
