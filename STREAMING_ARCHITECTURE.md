# OBS Studio Mobile Streaming Architecture

## Overview
OBS Studio Mobile has transitioned from a UI-simulated mockup (which performed TCP connection checks) to an actual Android Live Streaming pipeline using a hardware-accelerated RTMP/RTMPS client.

## Pipeline Architecture
The core broadcasting functionality relies on `StreamManager` using the `com.pedro.library.rtmp.RtmpCamera2` package for reliable, robust, hardware-accelerated encoding.

### Source Pipeline
1. **Camera Cast**: Handled via `OpenGlView` dynamically rendered with hardware processing. The `RtmpCamera2` implementation prepares a video buffer up to 4K resolution (based on settings) at 60 FPS, alongside AAC audio encoding.
2. **Screen Cast / WebView / Media**: Currently, multi-source OpenGL composition is highly experimental on mobile environments without custom OpenGL EGL context management. For stability, the actual RTMP pipeline natively captures the **Camera source**. Other view elements (Screen Cast, WebView, Media Cast, UI Overlays, Chroma Key) remain in the **simulated** Compose UI but are not fully pushed to the `RtmpCamera2` hardware buffers simultaneously. 

### Encoder Configuration
* **H.264 Video Codec**: Utilizes Qualcomm/MediaTek MediaCodec (hardware accelerated).
* **AAC Audio Codec**: Encodes at customizable bitrates (e.g., 160 kbps, 48kHz).
* **Low Latency & Configuration**: Modifiable FPS (30/60), target bitrate, resolution (up to 4K), and Keyframe Interval logic.

### Network Output & Fan-Out
When broadcasting is initiated, the application evaluates enabled destinations (YouTube, Twitch, Telegram, Custom). 
To prevent mobile device melt-down and bandwidth saturation, the pipeline establishes a **single primary RTMP/RTMPS stream**. Multi-platform simultaneous streaming requires a server-side RTMP restreamer (e.g., NGINX RTMP module). Additional selected destinations will be flagged appropriately with `Skipped (Multi-stream not implemented)` in the destination manager to accurately reflect output scope.

## Foreground Service
To prevent Android OS from sleeping the encoder:
1. `StudioBroadcastService` launches in the foreground.
2. An ongoing high-priority notification with controls (Mute/Stop) is attached.
3. MediaCodec/MediaProjection resources remain immune to standard Doze conditions.
