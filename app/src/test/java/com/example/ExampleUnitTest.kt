package com.example

import com.example.model.CustomMediaType
import com.example.model.LogoPosition
import com.example.util.FileUtils
import org.junit.Assert.*
import org.junit.Test

class ExampleUnitTest {

    @Test
    fun testFileUtilsDetectMediaType() {
        assertEquals(CustomMediaType.VIDEO, FileUtils.detectMediaType("intro.mp4", "video/mp4"))
        assertEquals(CustomMediaType.VIDEO, FileUtils.detectMediaType("stream.mkv", null))
        assertEquals(CustomMediaType.VIDEO, FileUtils.detectMediaType("web.webm", null))

        assertEquals(CustomMediaType.IMAGE, FileUtils.detectMediaType("logo.png", "image/png"))
        assertEquals(CustomMediaType.IMAGE, FileUtils.detectMediaType("banner.svg", "image/svg+xml"))
        assertEquals(CustomMediaType.IMAGE, FileUtils.detectMediaType("photo.jpg", "image/jpeg"))
        assertEquals(CustomMediaType.IMAGE, FileUtils.detectMediaType("anim.gif", "image/gif"))
    }

    @Test
    fun testFileUtilsGetFormatBadge() {
        assertEquals("PNG TRANSPARENT", FileUtils.getFormatBadge("watermark.png", "image/png"))
        assertEquals("SVG VECTOR", FileUtils.getFormatBadge("vector_logo.svg", "image/svg+xml"))
        assertEquals("GIF ANIMATED", FileUtils.getFormatBadge("alert.gif", "image/gif"))
        assertEquals("JPG PHOTO", FileUtils.getFormatBadge("thumbnail.jpg", "image/jpeg"))
        assertEquals("MP4 VIDEO", FileUtils.getFormatBadge("clip.mp4", "video/mp4"))
    }

    @Test
    fun testWatermarkCustomizationAndCountdown() {
        val config = com.example.model.CustomLogoConfig(
            isEnabled = true,
            showLogoFrame = false, // Blue frame removed!
            showBackground = true,
            backgroundColorMode = com.example.model.WatermarkBgColorMode.CYBER_PURPLE,
            textPosition = com.example.model.TextRelativePosition.UNDER,
            watermarkText = "CHAMPIONSHIP STAGE 1",
            isCountdownEnabled = true,
            countdownPosition = com.example.model.CountdownPosition.UNDER,
            countdownTotalSeconds = 10,
            countdownNextText = "GRAND FINALS LIVE"
        )

        assertFalse(config.showLogoFrame)
        assertTrue(config.showBackground)
        assertEquals(com.example.model.TextRelativePosition.UNDER, config.textPosition)
        assertEquals(com.example.model.CountdownPosition.UNDER, config.countdownPosition)
        assertEquals("GRAND FINALS LIVE", config.countdownNextText)
    }

    @Test
    fun testScrollingTextPermanentRunningAndMods() {
        val ticker = com.example.model.ScrollingTextConfig(
            isEnabled = true,
            isPermanentRunning = true,
            mod = com.example.model.MarqueeMod.NEWS,
            textContent = "BREAKING: Tournament playoffs underway",
            speed = com.example.model.MarqueeSpeed.FAST
        )

        assertTrue(ticker.isEnabled)
        assertTrue(ticker.isPermanentRunning)
        assertEquals(com.example.model.MarqueeMod.NEWS, ticker.mod)
        assertEquals(com.example.model.MarqueeSpeed.FAST, ticker.speed)
    }
}
