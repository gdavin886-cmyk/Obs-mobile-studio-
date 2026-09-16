package com.example

import com.example.model.CustomMediaType
import com.example.model.LogoPosition
import com.example.util.FileUtils
import com.example.viewmodel.StudioViewModel
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
    fun testStudioViewModelCustomLogoAndMediaCast() {
        val vm = StudioViewModel()

        // Test Custom Logo
        vm.setCustomLogoFile(
            uri = "content://media/external/images/media/123",
            fileName = "my_brand_logo.png",
            mimeType = "image/png"
        )
        val logoConfig = vm.customLogoConfig.value
        assertTrue(logoConfig.isEnabled)
        assertEquals("content://media/external/images/media/123", logoConfig.customImageUri)
        assertEquals("my_brand_logo.png", logoConfig.customImageName)

        vm.clearCustomLogoFile()
        assertNull(vm.customLogoConfig.value.customImageUri)
        assertNull(vm.customLogoConfig.value.customImageName)

        // Test Media Cast
        vm.setMediaCastSource(
            uri = "content://media/external/video/media/456",
            fileName = "gameplay_highlight.mp4",
            mediaType = CustomMediaType.VIDEO,
            mimeType = "video/mp4"
        )
        val mediaConfig = vm.mediaCastConfig.value
        assertEquals("content://media/external/video/media/456", mediaConfig.uri)
        assertEquals("gameplay_highlight.mp4", mediaConfig.fileName)
        assertEquals(CustomMediaType.VIDEO, mediaConfig.mediaType)

        vm.clearMediaCastSource()
        assertNull(vm.mediaCastConfig.value.uri)
        assertEquals("PRESENTATION_REEL_FINAL_4K.MP4", vm.mediaCastConfig.value.fileName)
    }
}
