package com.example

import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import androidx.lifecycle.Lifecycle
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.model.ConnectionStatus
import com.example.model.SceneId
import org.junit.Assert.*
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.annotation.Config
import org.robolectric.annotation.GraphicsMode

@RunWith(AndroidJUnit4::class)
@GraphicsMode(GraphicsMode.Mode.NATIVE)
@Config(sdk = [36])
class StudioRuntimePassTest {

  @get:Rule
  val composeRule = createAndroidComposeRule<MainActivity>()

  @Test
  fun testStudioComprehensiveLifecycle() {
    composeRule.waitForIdle()

    // 1. Verify Main Dashboard is mounted
    composeRule.onNodeWithTag("studio_dashboard_screen").assertExists()
    val activity = composeRule.activity
    assertNotNull(activity)

    // 2. Select Website scene
    composeRule.onNodeWithTag("scene_card_WEB_CAST").performClick()
    composeRule.waitForIdle()

    // 3. Select Camera scene back
    composeRule.onNodeWithTag("scene_card_CAMERA_GREENSCREEN").performClick()
    composeRule.waitForIdle()

    // 4. Toggle Studio Mode (Preview / Program side by side)
    composeRule.onNodeWithTag("toggle_studio_mode_btn").performClick()
    composeRule.waitForIdle()

    // 5. Test transition studio
    composeRule.onNodeWithTag("studio_transition_btn").performClick()
    composeRule.waitForIdle()

    // 6. Test Start Live and Stop Live
    composeRule.onNodeWithTag("toggle_live_btn").performClick()
    composeRule.waitForIdle()
    composeRule.onNodeWithTag("toggle_live_btn").performClick()
    composeRule.waitForIdle()

    // 7. Test all scenes switching & camera surface destruction/recreation
    composeRule.onNodeWithTag("scene_card_SCREEN_CAST").performClick()
    composeRule.waitForIdle()

    composeRule.onNodeWithTag("scene_card_MEDIA_CAST").performClick()
    composeRule.waitForIdle()

    composeRule.onNodeWithTag("scene_card_STARTING_SOON").performClick()
    composeRule.waitForIdle()

    composeRule.onNodeWithTag("scene_card_WEB_CAST").performClick()
    composeRule.waitForIdle()

    composeRule.onNodeWithTag("scene_card_CAMERA_GREENSCREEN").performClick()
    composeRule.waitForIdle()

    // 8. Background & Foreground lifecycle
    composeRule.activityRule.scenario.moveToState(Lifecycle.State.STARTED)
    composeRule.activityRule.scenario.moveToState(Lifecycle.State.RESUMED)
    composeRule.waitForIdle()

    // 9. Activity recreation (simulates screen rotation and configuration changes)
    composeRule.activityRule.scenario.recreate()
    composeRule.waitForIdle()
    composeRule.onNodeWithTag("studio_dashboard_screen").assertExists()
  }
}
