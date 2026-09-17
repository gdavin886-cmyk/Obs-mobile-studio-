# Release v1.1.0

## 🚀 Update Information
* **Real RTMP Network Verification**: Replaced simulated connection tasks with real TCP socket network connections.
* **Server & Key Validation**: The app now actively verifies that the Server URL is reachable (port 443 for RTMPS, 1935 for RTMP) and that the Stream Key is not empty before initiating a broadcast.
* **Accurate Status Reporting**: Broadcasting status now accurately reflects real network responses (e.g., "Verified! Connected to live.twitch.tv:443" or "Network Error").

## 🛠️ Fixing Summary
* **MESA Rendernode Crash Resolved**: Fixed a critical crash on specific emulators and virtual devices by forcing software rendering (`LAYER_TYPE_SOFTWARE`) in the WebView components, bypassing the missing GPU hardware rendernode issue.
* **Telegram Connection Unblocked**: Removed the simulated artificial block on Telegram and Custom RTMP connections, allowing successful real-world connections.
* **UI Cleanup**: Removed the experimental RTMP Network Status overlay to maintain a clean and unobtrusive dashboard interface.
