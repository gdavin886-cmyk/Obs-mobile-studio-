package com.example

import android.app.Application
import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.example.model.*
import com.example.viewmodel.StudioViewModel
import org.junit.Assert.*
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [36])
class ExampleRobolectricTest {

  @Test
  fun `read string from context`() {
    val context = ApplicationProvider.getApplicationContext<Context>()
    val appName = context.getString(R.string.app_name)
    assertEquals("OBS Studio Mobile", appName)
  }

  @Test
  fun `verify StudioViewModel logo and scrolling text ticker state`() {
    val app = ApplicationProvider.getApplicationContext<Application>()
    val vm = StudioViewModel(app)

    assertNotNull(vm.customLogoConfig.value)
    assertNotNull(vm.scrollingTextConfig.value)

    // Test updating scrolling text config
    val updatedTicker = ScrollingTextConfig(
        isEnabled = true,
        isPermanentRunning = true,
        textContent = "LIVE NOW",
        mod = MarqueeMod.NEWS
    )
    vm.updateScrollingTextConfig(updatedTicker)
    assertEquals("LIVE NOW", vm.scrollingTextConfig.value.textContent)
    assertTrue(vm.scrollingTextConfig.value.isPermanentRunning)

    // Test countdown start
    vm.startWatermarkCountdown(15, "NEXT ROUND LIVE")
    assertTrue(vm.customLogoConfig.value.isCountdownEnabled)
    assertEquals(15, vm.customLogoConfig.value.countdownTotalSeconds)
    assertEquals("NEXT ROUND LIVE", vm.customLogoConfig.value.countdownNextText)

    vm.cancelWatermarkCountdown()
    assertFalse(vm.customLogoConfig.value.isCountdownRunning)
  }
}
