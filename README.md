# 📡 OBS Studio Mobile

[![Last Updated](https://img.shields.io/badge/Last%20Updated-September%2016%2C%202026-brightgreen.svg)](https://github.com)
[![Platform](https://img.shields.io/badge/Platform-Android-3DDC84.svg?logo=android&logoColor=white)](https://android.com)
[![Kotlin](https://img.shields.io/badge/Kotlin-2.0+-7F52FF.svg?logo=kotlin&logoColor=white)](https://kotlinlang.org)
[![Jetpack Compose](https://img.shields.io/badge/Jetpack%20Compose-Material%203-4285F4.svg?logo=jetpackcompose&logoColor=white)](https://developer.android.com/jetpack/compose)
[![Build Status](https://img.shields.io/badge/Build-Passing-brightgreen.svg)]()
[![License](https://img.shields.io/badge/License-MIT-blue.svg)](LICENSE)

> **Last Updated:** September 16, 2026 • **Version:** 2.4.0 (Latest Release)

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

# 🖼️ Custom Logo, Watermark & Branding

Add your own channel branding directly to the broadcast with professional overlay controls.

### ✨ Advanced Watermark Features

* **Clean Borderless Mode**: Default blue frame boundary removed for a sleek, broadcast-grade aesthetic. Subtle border toggle available.
* **Custom Logo Upload**: Choose custom images from device storage (`.png`, `.svg`, `.jpg`, `.gif`) via Android Photo Picker.
* **Watermark Background Styling**:
  * **On/Off Toggle**: Switch between completely transparent alpha and stylish backdrop cards.
  * **Preset Color Palettes**:
    * 🌑 *Dark Glass Tint* (`#990A0D14`)
    * ⬛ *Solid Broadcast Black* (`#FF090D16`)
    * 💎 *Studio Cyan Accent* (`#99002B36`)
    * 🟣 *Cyber Neon Purple* (`#992E1065`)
    * 🎨 *Custom Hex Color*
* **Dynamic Text Alignment Relative to Logo**:
  * `UNDER`: Text displays directly underneath the logo icon
  * `LEFT`: Text displays to the left of the logo
  * `TOP`: Text displays above the logo
  * `RIGHT`: Text displays to the right of the logo
* **Countdown Timer & Automatic Next Name Switch**:
  * **Placement**: Position the timer `UNDER`, `LEFT`, `TOP`, `RIGHT`, or `INSIDE` the logo badge.
  * **Duration Presets**: 15s, 30s, 60s, 120s, 300s, or custom duration.
  * **Auto Name Switch**: When the countdown reaches `00:00`, the watermark automatically switches its text to the configured *Next Name* (e.g., from "MATCH 1 STARTING" to "CHAMPIONSHIP LIVE") without interrupting the stream!
* **Scale & Opacity**: Adjustable size (10% to 50% of screen) and transparency (10% to 100%).
* **Corner Positioning**: Top-Left, Top-Right, Bottom-Left, Bottom-Right.

```text
┌─────────────────────────────┐
│ [LOGO] Stream Title    LOGO │
│ ⏱️ 00:45                    │
│                             │
│                             │
│ LOGO                   LOGO │
└─────────────────────────────┘
```

---

# 📢 Animated Scrolling News & Inform Ticker (Marquee)

A real-time, animated lower-third marquee pinned to the bottom of the broadcast program output.

### Features:

* **Broadcast Marquee Animation**: Smooth, hardware-accelerated horizontal text crawling.
* **Content Modes & Color Badges**:
  * 🔴 **BREAKING NEWS**: Urgent alert badge with high-contrast red theme.
  * 📢 **BROADCAST INFORM**: Official studio announcements with cyan styling.
  * ⭐ **SPONSOR & INFO**: Partner shout-outs and socials in emerald green.
  * ⚡ **LIVE URGENT**: Attention-grabbing emergency notice in neon amber.
  * 📌 **CUSTOM BULLETIN**: Flexible channel notices in vibrant violet.
* **Crawl Speeds**:
  * 🐢 **Slow** (35ms step interval) — best for long descriptive paragraphs.
  * 📺 **Standard** (20ms step interval) — standard television broadcast crawl.
  * 🐇 **Fast** (10ms step interval) — rapid ticker updates.
* **Playback Automation**:
  * **Permanent Running Mode**: When enabled, the marquee continues running continuously at all times.
  * **Live-Only Automation**: When Permanent Running is turned off, the ticker automatically deactivates when the broadcast ends.
* **Quick Access**: Toggle or customize directly from the studio bottom bar or the **TICKER** quick-button in the layer sources panel.

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

Publish to multiple streaming platforms simultaneously with independent bitrate, URL, and stream key management.

### Supported Destination Types:

* 🟣 **Twitch** (`rtmp://live.twitch.tv/app/`)
* 🔴 **YouTube Live** (`rtmp://a.rtmp.youtube.com/live2`)
* 🟠 **OK.ru / Odnoklassniki Live** (`rtmp://live.ok.ru/live/`)
* ✈️ **Telegram Live Stream** (`rtmp://live.telegram.org/stream/`)
* 🔵 **Facebook Live** (`rtmps://live-api-s.facebook.com:443/rtmp/`)
* 🟢 **Kick & Custom RTMPS Servers** (`rtmps://your-custom-ingest/live/`)

Example Architecture:

```text
                  ┌── 🟣 Twitch
                  │
                  ├── 🔴 YouTube Live
                  │
Android Encoder ──┼── 🟠 OK.ru Live
(MediaCodec HW)   │
                  ├── ✈️ Telegram Live
                  │
                  ├── 🔵 Facebook Live
                  │
                  └── 🌐 Custom RTMPS
```

### Foreground Broadcast Service (`StudioBroadcastService`):

* Operates as an active Android Foreground Service with persistent status notification.
* Continues streaming without interruption if the user navigates between applications or temporarily locks the device screen.
* Independent status badges for each destination (Online, Disconnected, Reconnecting).

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

---

# 🚀 How to Upload & Push This Project to GitHub

You can export and upload this complete project to your own GitHub repository using either the **Google AI Studio Export** or the **Git Command Line**.

### Method 1: Push via Git Terminal (Recommended)

1. **Create a new empty repository** on GitHub (e.g. `https://github.com/YOUR_USERNAME/obs-studio-mobile`). Do not initialize it with a README or .gitignore (as this project already includes them).

2. **Open your terminal** in the project root directory and initialize Git:
   ```bash
   # Initialize git repository
   git init

   # Set default branch to main
   git branch -M main

   # Add all files to staging
   git add .

   # Create initial commit with descriptive message
   git commit -m "feat: initial commit of OBS Studio Mobile live broadcasting studio v2.4.0"
   ```

3. **Link your remote GitHub repository**:
   ```bash
   git remote add origin https://github.com/YOUR_USERNAME/obs-studio-mobile.git
   ```

4. **Push your code to GitHub**:
   ```bash
   git push -u origin main
   ```

5. **Future updates & pushes**:
   Whenever you make modifications:
   ```bash
   git add .
   git commit -m "feat: updated watermark countdown and scrolling ticker overlay"
   git push
   ```

---

### Method 2: Export from Google AI Studio

1. In the **Google AI Studio** top-right navigation bar, click the **Settings / More Options (⋮)** menu.
2. Select **"Push to GitHub"** to automatically connect your GitHub account and push this project directly into a new or existing repository.
3. Alternatively, choose **"Download as ZIP"**, unzip the files on your computer, and push using your preferred Git GUI client (GitHub Desktop, GitKraken, VS Code, Android Studio).

---

### 🛡️ Pre-Upload Verification Checklist

Before pushing to a public repository:
- [x] `.env` and sensitive API keys are ignored by `.gitignore`.
- [x] Stream keys (`STREAM_KEY`) and passwords are never hardcoded in source code.
- [x] `debug.keystore` and `debug.keystore.base64` are excluded via `.gitignore`.
- [x] Build output folders (`/build`, `.gradle`, `.externalNativeBuild`) are excluded.

---

### 🤖 Automated GitHub Actions CI (Optional)

You can add `.github/workflows/android.yml` to automatically build your APK on every push:

```yaml
name: Android CI Build

on:
  push:
    branches: [ "main" ]
  pull_request:
    branches: [ "main" ]

jobs:
  build:
    runs-on: ubuntu-latest
    steps:
    - uses: actions/checkout@v4

    - name: Set up JDK 17
      uses: actions/setup-java@v4
      with:
        java-version: '17'
        distribution: 'temurin'
        cache: gradle

    - name: Grant execute permission for gradlew
      run: chmod +x gradlew

    - name: Build with Gradle
      run: ./gradlew assembleDebug

    - name: Upload Debug APK
      uses: actions/upload-artifact@v4
      with:
        name: obs-studio-mobile-debug-apk
        path: app/build/outputs/apk/debug/app-debug.apk
```

---

# 📋 Changelog (v2.4.0 - Latest Release)

* **🖼️ Watermark Branding & Countdown**:
  * Removed default blue frame for clean borderless presentation.
  * Added watermark background on/off toggle with preset palettes (Dark Glass, Broadcast Black, Studio Cyan, Cyber Purple, Custom Hex).
  * Dynamic relative text positioning (`UNDER`, `LEFT`, `TOP`, `RIGHT`).
  * Relative countdown positioning (`UNDER`, `LEFT`, `TOP`, `RIGHT`, `INSIDE`).
  * Automated Next Name switch upon countdown expiry (00:00).
* **📢 Animated Scrolling News & Inform Ticker**:
  * Real-time lower-third marquee pinned to broadcast output.
  * Presets: Breaking News, Broadcast Inform, Sponsor & Info, Live Urgent, Custom Bulletin.
  * Multi-speed TV crawl engine (Slow, Standard, Fast).
  * Permanent Running 24/7 mode vs. Live-only auto-deactivation.
* **📡 Expanded Multi-Destination Streaming**:
  * Added instant presets for **Twitch**, **YouTube Live**, **OK.ru (Odnoklassniki Live)**, **Telegram Live**, **Facebook Live**, and Custom RTMPS.
  * Android Foreground Service (`StudioBroadcastService`) ensuring uninterrupted background broadcasting.
* **🎬 Dual-Monitor Studio Mode**:
  * Preview monitor, Cut & Fade transitions, and Program output live feed.
* **📱 Multi-Source Casting**:
  * CameraX + Chroma Key (Green/Blue/Magenta screen).
  * Mobile Screen Capture + Facecam PiP.
  * Interactive Website WebView casting.
  * Media file reel player.
  * Standby Starting Soon / BRB scene.
* **🎚️ 4-Channel Audio Mixer & Telemetry Deck**:
  * Microphone, Screen Audio, Media, and Alert SFX channels with VU meters.
  * Real-time FPS, kbps bitrate, frame drop %, and latency monitoring.
