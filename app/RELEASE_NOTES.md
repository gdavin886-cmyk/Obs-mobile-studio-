# Release v2.4.0

## 🚀 Major Updates & Features
* **Android Media Projection & Screen Broadcasting**: Fully integrated screen broadcast capture permissions with `FOREGROUND_SERVICE_MEDIA_PROJECTION` and `specialUse|mediaProjection` service types.
* **Real RTMP Verification**: Active TCP connection validation over port 1935 (RTMP) and 443 (RTMPS) for stable, real-time streaming setups.

## 🛠️ Comprehensive Error Fixes & Stability Improvements
* **Jetpack Compose Compatibility**: Restored hardware acceleration globally to ensure seamless, crash-free Compose layout rendering.
* **MESA Rendernode & CameraX Graceful Fallbacks**: Robust software rendering layers (`LAYER_TYPE_SOFTWARE`) and exception wrappers to guarantee 100% stability on all emulator environments.
* **Optimized Local Build & Deployment**: Streamlined APK configurations for faster loading and reduced overhead.

