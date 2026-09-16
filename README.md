OBS Studio Mobile
Obs studio similar application Website site page casting, mobile screen casting, cemara casting, file video or image casting with my custom logo including to publish to platforms like Twitch, YouTube, and Facebook in real-time. It should feature an intuitive dashboard with scene switching and hardware encoding support.multiple stream output destinations.such as RTMPS support and low latency streaming for high-quality broadcasts.pop up gif video overlays triggered by viewer engagement or specific stream events.chomcuta keying filter for greenscreen backgrounds.
Build an Android app



summarize_auto
Action history
chevron_right
Here are key actions taken for the app:


I have implemented the OBS Studio Mobile live broadcasting application in Kotlin and Jetpack Compose.
What Was Built:
Intuitive Studio Dashboard & Scene Switching:
Program & Preview Monitors: 16:9 widescreen canvas with studio tally borders (Red when Live on air, Amber in Preview staging mode, Cyan in Standby).
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
