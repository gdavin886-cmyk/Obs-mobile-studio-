# 📡 OBS Studio Mobile

A powerful **OBS Studio-inspired mobile broadcasting application for Android**, built with **Kotlin + Jetpack Compose**, designed to turn an Android phone into a portable live production studio.

Capture your **camera, mobile screen, websites, videos, and images**, compose them into professional scenes, apply chroma-key effects and branded overlays, and publish live broadcasts to multiple RTMPS destinations such as **Twitch, YouTube Live, Facebook Live, and custom RTMPS servers**.

> ⚠️ This project is an independent mobile broadcasting application inspired by desktop production workflows. It is not affiliated with or endorsed by OBS Studio.

---

## ✨ Features

### 🎬 Professional Studio Dashboard

The application provides an intuitive production dashboard with:

* Program monitor
* Preview monitor
* Studio Mode
* Scene switching
* Cut transition
* Fade transition
* Live/Preview/Standby status indicators
* Real-time stream telemetry
* Broadcast controls
* Audio mixer
* Encoder configuration

The production workflow is designed around the familiar:

**Preview → Transition → Program**

model.

---

# 🎥 Broadcasting Scenes

The application includes several ready-to-use scenes.

### 🎬 Camera + Chroma Key

Use the Android camera as a live video source.

Features:

* CameraX integration
* Front/rear camera selection
* Green-screen background removal
* Virtual studio background
* Blue-screen support
* Magenta-screen support
* Adjustable chroma-key parameters
* Custom logo overlays

Example:

```text
Camera
   ↓
Chroma Key Filter
   ↓
Virtual Background
   ↓
Logo / Graphics
   ↓
Program Output
```

---

### 📱 Mobile Screen Casting

Broadcast the Android device screen.

Supports:

* Android MediaProjection
* Game streaming
* App demonstrations
* Tutorials
* Mobile gameplay
* Screen + facecam PiP
* Screen audio
* Microphone audio

Example layout:

```text
┌──────────────────────────────┐
│                              │
│      MOBILE SCREEN           │
│                              │
│                              │
│                    ┌──────┐  │
│                    │ FACE │  │
│                    │ CAM  │  │
│                    └──────┘  │
└──────────────────────────────┘
```

---

### 🌐 Website Page Casting

Render a website directly inside the broadcast.

Useful for:

* Websites
* Live dashboards
* News pages
* Online presentations
* Twitch chat
* Live tickers
* Browser-based content

The source uses an Android WebView and can be composed into the broadcast scene.

---

### 📼 Video & Image Casting

Broadcast local media files.

Supported sources include:

* MP4
* WebM
* Images
* Local media
* Video reels
* Promotional clips
* Background videos

Media controls include:

* Play
* Pause
* Seek
* Restart
* Loop
* Volume
* Full-screen composition

---

### ⏱️ Starting Soon / BRB

Built-in standby scenes for professional broadcasts.

Example:

```text
╔══════════════════════════════╗
║                              ║
║       STREAM STARTING        ║
║           SOON               ║
║                              ║
║       @YOURCHANNEL           ║
║                              ║
║       ENCODER: READY         ║
║                              ║
╚══════════════════════════════╝
```

---

# 🟢 Chroma Key / Green Screen

Professional green-screen functionality allows camera footage to be composited over virtual backgrounds.

### Keying Controls

* Similarity threshold
* Edge smoothness
* Color spill reduction
* Key color selection
* Transparency
* Background selection

### Presets

```text
🟢 Green Screen
🔵 Blue Screen
🟣 Magenta Screen
```

### Background Modes

```text
Virtual Studio
Solid Background
Transparent Alpha
Custom Image
Custom Video
```

---

# 🖼️ Custom Logo & Branding

Add your own branding directly to the broadcast.

Features:

* Custom logo
* Watermark
* Channel name
* Social media handle
* Adjustable opacity
* Adjustable scale
* Corner positioning

Supported positions:

```text
┌─────────────────────────────┐
│ LOGO                    LOGO│
│                             │
│                             │
│                             │
│ LOGO                    LOGO│
└─────────────────────────────┘
```

---

# 🎞️ GIF / Video Pop-Up Overlays

Create animated engagement overlays that appear during live broadcasts.

Possible triggers:

* New follower
* Subscriber
* Gift subscription
* Super Chat
* Donation
* Raid
* Custom event

Example:

```text
┌──────────────────────────────┐
│                              │
│        🎉 NEW FOLLOWER       │
│                              │
│          @username           │
│                              │
└──────────────────────────────┘
```

Overlay components can include:

* GIF animation
* Video animation
* Avatar
* Username
* Message
* Sound effect
* Animated badge
* Custom graphics

A custom event builder can be used to associate stream events with specific overlays.

---

# 🎚️ Audio Mixer

A built-in multi-channel audio mixer provides independent control over broadcast audio.

### Audio Sources

| Channel | Source       |
| ------- | ------------ |
| 🎙️ 1   | Microphone   |
| 📱 2    | Screen Audio |
| 🎬 3    | Media Player |
| 🔔 4    | Alert SFX    |

Features:

* Volume faders
* Mute
* Peak indicators
* VU meters
* Peak hold
* Independent source control

---

# 📡 Multi-Destination Streaming

Publish to multiple streaming platforms simultaneously.

Supported destination types:

```text
Twitch
YouTube Live
Facebook Live
Custom RTMPS
```

Example:

```text
                  ┌── Twitch
                  │
Android Encoder ──┼── YouTube Live
                  │
                  ├── Facebook Live
                  │
                  └── Custom RTMPS
```

Each destination should maintain its own:

* RTMPS URL
* Stream key
* Connection state
* Bitrate
* Error state
* Reconnect status

> Platform APIs, authentication requirements, and streaming policies can change. Production integrations should use each platform's current official requirements.

---

# 🚀 RTMPS

The streaming pipeline is designed around RTMP/RTMPS publishing.

Example destination:

```text
rtmps://example.com/live
```

Configuration:

```text
Server URL
Stream Key
Bitrate
Resolution
Framerate
Audio Bitrate
Keyframe Interval
```

---

# ⚡ Low-Latency Streaming

Broadcast configurations can be optimized for low-latency streaming.

Configuration options include:

* Low-latency mode
* RTMPS
* Encoder tuning
* Keyframe interval
* Buffer configuration
* Adaptive bitrate configuration

The exact latency achievable depends on the Android device, network, encoder, ingest server, and destination platform.

---

# 🧠 Hardware Encoding

Use Android hardware video encoders where supported.

Supported codec profiles may include:

```text
H.264 / AVC
HEVC / H.265
AV1
```

The application detects available MediaCodec encoders and selects an appropriate hardware encoder.

Example:

```text
Camera / Screen / Media
          ↓
     Scene Composer
          ↓
     Video Encoder
          ↓
       MediaCodec
          ↓
        RTMPS
```

---

# 🎞️ Resolution Profiles

Preset broadcast profiles include:

| Profile   | Resolution |              FPS |
| --------- | ---------: | ---------------: |
| HD        |   1280×720 |               30 |
| HD60      |   1280×720 |               60 |
| Full HD   |  1920×1080 |               30 |
| Full HD60 |  1920×1080 |               60 |
| 4K        |  3840×2160 | Device dependent |

Bitrate configuration:

```text
1,000 kbps ─────────────── 12,000 kbps
```

Actual maximum resolution, FPS, codec and bitrate depend on device hardware and platform ingest limits.

---

# 📊 Live Telemetry

Monitor broadcast health directly from the studio dashboard.

Metrics include:

```text
FPS
Bitrate
Dropped Frames
RTMPS Latency
Encoder Status
Connection Status
Audio Level
```

Example:

```text
LIVE ●

FPS       59.8
BITRATE   7,842 kbps
DROPPED   0.3%
LATENCY   148 ms
ENCODER   H.264 HW
```

---

# 🎛️ Scene System

Scenes are composited from multiple sources.

Example:

```text
Scene
 ├── Camera
 ├── Screen Capture
 ├── Website
 ├── Video
 ├── Image
 ├── Logo
 ├── Lower Third
 ├── Alert Overlay
 └── Chroma Key
```

Scene switching supports:

* Instant Cut
* Fade
* Preview
* Program
* Scene carousel
* Active scene indication

---

# 🏗️ Architecture

The application is designed using modern Android architecture.

```text
Android App
│
├── Jetpack Compose UI
│
├── ViewModel
│
├── Scene Manager
│
├── Media Source Manager
│   ├── CameraX
│   ├── MediaProjection
│   ├── WebView
│   └── Local Media
│
├── Scene Composer
│   ├── Chroma Key
│   ├── Logo
│   ├── Text
│   ├── GIF
│   ├── Video Overlay
│   └── Transitions
│
├── Audio Mixer
│
├── Hardware Encoder
│   └── Android MediaCodec
│
└── Streaming Engine
    ├── RTMP
    ├── RTMPS
    └── Multi-Destination Output
```

---

# 🧰 Technology Stack

### Android

* Kotlin
* Jetpack Compose
* Android SDK
* Coroutines
* StateFlow
* ViewModel

### Media

* CameraX
* MediaProjection
* MediaCodec
* MediaExtractor
* MediaMuxer
* Android Audio APIs
* WebView

### Streaming

* RTMP
* RTMPS
* H.264 / AVC
* HEVC / H.265
* AV1 where supported

---

# 📁 Suggested Project Structure

```text
app/
├── src/
│   └── main/
│       ├── java/
│       │   └── com/example/mobileobs/
│       │       ├── MainActivity.kt
│       │       │
│       │       ├── ui/
│       │       │   ├── dashboard/
│       │       │   ├── scenes/
│       │       │   ├── mixer/
│       │       │   ├── settings/
│       │       │   └── components/
│       │       │
│       │       ├── scene/
│       │       │   ├── Scene.kt
│       │       │   ├── SceneManager.kt
│       │       │   └── SceneComposer.kt
│       │       │
│       │       ├── capture/
│       │       │   ├── CameraCapture.kt
│       │       │   ├── ScreenCapture.kt
│       │       │   ├── WebCapture.kt
│       │       │   └── MediaCapture.kt
│       │       │
│       │       ├── encoder/
│       │       │   ├── VideoEncoder.kt
│       │       │   └── AudioEncoder.kt
│       │       │
│       │       ├── streaming/
│       │       │   ├── StreamDestination.kt
│       │       │   ├── RtmpPublisher.kt
│       │       │   └── MultiStreamManager.kt
│       │       │
│       │       ├── overlays/
│       │       │   ├── LogoOverlay.kt
│       │       │   ├── GifOverlay.kt
│       │       │   └── AlertOverlay.kt
│       │       │
│       │       └── audio/
│       │           └── AudioMixer.kt
│       │
│       └── res/
│
├── build.gradle.kts
└── AndroidManifest.xml
```

---

# 🔐 Stream Security

Stream keys should never be hard-coded into the application.

Recommended storage:

```text
Android Keystore
        ↓
Encrypted Preferences
        ↓
Stream Configuration
```

Users should be able to:

* Add destination
* Edit destination
* Enable/disable destination
* Test connection
* Delete destination
* Hide stream keys

---

# 📱 Permissions

Depending on enabled features, the application may request:

```text
CAMERA
RECORD_AUDIO
INTERNET
FOREGROUND_SERVICE
FOREGROUND_SERVICE_CAMERA
FOREGROUND_SERVICE_MICROPHONE
FOREGROUND_SERVICE_MEDIA_PROJECTION
POST_NOTIFICATIONS
```

Screen capture should use Android's user-authorized **MediaProjection** flow.

---

# 🎨 Custom Branding

The application should support a configurable brand identity.

Replace the default application logo with your custom logo in:

```text
app/src/main/res/
```

Recommended assets:

```text
ic_launcher.png
logo.png
logo_vector.xml
splash_logo.png
```

Branding can also be applied to:

* App icon
* Splash screen
* Studio dashboard
* Broadcast watermark
* Lower thirds
* Alert animations

---

# 🖥️ Dashboard Concept

```text
┌─────────────────────────────────────────┐
│ MOBILE OBS                     ● LIVE   │
├─────────────────────────────────────────┤
│                                         │
│       ┌────────── PROGRAM ──────────┐   │
│       │                             │   │
│       │        LIVE OUTPUT          │   │
│       │                             │   │
│       └─────────────────────────────┘   │
│                                         │
│       ┌────────── PREVIEW ──────────┐    │
│       │                             │    │
│       │       NEXT SCENE            │    │
│       │                             │    │
│       └─────────────────────────────┘   │
│                                         │
│ [Camera] [Screen] [Web] [Media] [BRB] │
│                                         │
│ [ CUT ]             [ FADE ]            │
├─────────────────────────────────────────┤
│ MIC ████████   SCREEN ██████            │
│ MEDIA █████    ALERT ███                │
├─────────────────────────────────────────┤
│ FPS 60 | 7.8Mbps | 0.3% Drop | 148ms   │
└─────────────────────────────────────────┘
```

---

# 🛠️ Build

Clone the repository:

```bash
git clone https://github.com/YOUR_USERNAME/mobile-obs.git
cd mobile-obs
```

Open the project in Android Studio.

Then:

```bash
./gradlew assembleDebug
```

APK output:

```text
app/build/outputs/apk/debug/app-debug.apk
```

For a release build:

```bash
./gradlew assembleRelease
```

A release APK should be signed using your Android release keystore before distribution.

---

# ▶️ Running the Application

1. Install Android Studio.
2. Open the project.
3. Allow Gradle to synchronize.
4. Connect an Android device or start an emulator.
5. Enable required permissions.
6. Run the application.
7. Configure your broadcast destination.
8. Select a scene.
9. Configure encoder settings.
10. Preview the broadcast.
11. Start streaming.

---

# 🔑 Streaming Configuration

Example configuration:

```text
Destination:
YouTube Live

Protocol:
RTMPS

Server:
<platform ingest URL>

Stream Key:
<private stream key>

Resolution:
1920x1080

FPS:
60

Video Codec:
H.264

Bitrate:
6000 kbps

Audio:
AAC

Low Latency:
Enabled
```

**Never commit real stream keys to GitHub.**

Use placeholders:

```text
RTMPS_URL=your_server_url
STREAM_KEY=your_stream_key
```

---

# 🌐 Platform Integration

The application architecture supports destination adapters for:

* Twitch
* YouTube Live
* Facebook Live
* Custom RTMPS endpoints

Platform-specific authentication and APIs should be implemented separately from the core streaming engine.

Recommended architecture:

```text
Streaming Engine
       │
       ├── TwitchDestination
       ├── YouTubeDestination
       ├── FacebookDestination
       └── CustomRtmpDestination
```

This keeps platform-specific authentication and configuration isolated.

---

# 📈 Future Roadmap

## Phase 1

* [x] Studio dashboard
* [x] Scene switching
* [x] Camera scene
* [x] Screen casting
* [x] WebView scene
* [x] Media scene
* [x] Chroma key controls
* [x] Logo overlay
* [x] Audio mixer UI
* [x] Streaming configuration UI

## Phase 2

* [ ] Production RTMPS publisher
* [ ] Hardware encoder integration
* [ ] Multi-destination streaming
* [ ] Persistent stream profiles
* [ ] Automatic reconnect
* [ ] Bitrate monitoring
* [ ] Dropped-frame monitoring

## Phase 3

* [ ] Twitch integration
* [ ] YouTube Live integration
* [ ] Facebook Live integration
* [ ] Viewer event API
* [ ] Real-time chat
* [ ] Follow/subscription alerts
* [ ] Donation alerts

## Phase 4

* [ ] Advanced GPU compositor
* [ ] Animated scene transitions
* [ ] Custom shaders
* [ ] Advanced chroma key
* [ ] LUT filters
* [ ] Picture-in-picture editor
* [ ] Lower-third designer
* [ ] Stream replay buffer

---

# ⚠️ Production Considerations

A functional UI prototype and a production-grade mobile broadcaster are different levels of implementation.

For production deployment, the following components require careful device testing:

* MediaCodec compatibility
* Hardware encoder availability
* Android MediaProjection restrictions
* Camera orientation
* Audio synchronization
* Hardware HEVC/AV1 support
* RTMPS transport
* Multiple simultaneous encoders/outputs
* Thermal throttling
* Battery consumption
* Network interruptions
* Background execution restrictions

Not every Android device supports every resolution, codec, frame rate, or simultaneous stream configuration.

---

# 🤝 Contributing

Contributions are welcome.

```bash
fork → branch → change → test → pull request
```

Please create an issue before major architectural changes.

---

# 📄 License

Choose an appropriate open-source license before publishing the repository.

For example:

```text
MIT License
```

If the application contains third-party libraries, verify and preserve their individual license requirements.

---

# ⚖️ Disclaimer

This project is an independent Android broadcasting application.

It is **not affiliated with, sponsored by, or endorsed by OBS Studio**, Twitch, YouTube, Facebook/Meta, or other streaming platforms.

Users are responsible for complying with the terms, policies, copyright requirements, and API requirements of the services they stream to.

---

# ⭐ Project Goal

The goal of **OBS Studio Mobile** is to provide creators with a portable production environment capable of combining:

```text
📱 Screen Capture
🎥 Camera
🌐 Websites
📼 Media
🖼️ Images
🟢 Chroma Key
🎞️ Animated Overlays
🎙️ Audio Mixing
🏷️ Branding
⚡ Hardware Encoding
📡 RTMPS
🌎 Multiple Destinations
```

into one Android-based live production workflow.

**Turn your Android device into a portable live broadcasting studio. 📡🎥📱**
Studio Mode: Dual-monitor workflow allowing you to stage scenes in Preview before triggering Cut or Fade transitions to the Live Program.
Instant Scene Carousel: Switch between 5 broadcasting scenes:
🎬 Camera + Chroma Key: CameraX video feed composited over a virtual studio room.
📱 Mobile Screen Casting: Screen capture with game simulation and a Picture-in-Picture (PiP) facecam.
🌐 Website Page Casting: Real interactive WebView for streaming websites, live tickers, or Twitch chat popouts.
📼 File Video & Media Casting: Video reel player with playback scrubbers and broadcast lower-third graphics.
⏱️ Starting Soon / BRB: Pulsating standby scene with social media handles and encoder status.
Chroma Keying Filter for Green Screen Backgrounds:
Key color selection with presets for Green Screen (#00FF00), Blue Screen (#0000FF), and Magenta.
Granular control over Similarity Threshold, Edge Smoothness, and Color Spill Reduction.
Background compositing with options for Virtual Studio Room, Solid Dark Surface, or Transparent Alpha.
Custom Logo Watermark & Overlays:
Dedicated watermark overlay supporting corner positioning (Top-Left, Top-Right, Bottom-Left, Bottom-Right).
Adjustable size scaling, opacity levels, and custom channel branding text.
Multi-Destination RTMPS & Hardware Encoding:
Simultaneous multi-output publishing to Twitch, YouTube Live, Facebook Live, and Custom RTMPS.
Hardware encoding pipeline configuring MediaCodec H.264 (HW), MediaCodec HEVC/H.265 (HW), and AV1 (HW).
Resolution and framerate profiles (1080p 60FPS, 1080p 30FPS, 720p 60FPS, 4K) with a 1,000–12,000 kbps bitrate fader and a Low-Latency Streaming (LL-HLS / RTMPS) toggle.
Viewer Engagement Pop-Up Video Overlays:
Animated alert overlays triggered by stream events with glowing badges, donor avatars, sound indicators, and personalized chat messages.
Built-in event triggers for New Followers, Tier 1 Subscribers, $100 Super Chats, Viewer Raids, and Community Gift Subs, plus a custom event builder.
Studio Audio Mixer & Telemetry Deck:
4-channel audio console (Microphone, Screen Audio, Media Player, Alert SFX) with dynamic VU meters, peak hold indicators, volume faders, and mute buttons.
Live broadcast telemetry monitoring real-time FPS, kbps bitrate, dropped frame rate percentage, RTMPS latency (ms), and hardware encoder status.
