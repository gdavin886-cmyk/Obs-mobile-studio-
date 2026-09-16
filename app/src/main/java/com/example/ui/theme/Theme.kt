package com.example.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val StudioColorScheme = darkColorScheme(
    primary = StudioCyan,
    onPrimary = StudioObsidian,
    primaryContainer = StudioPurple,
    onPrimaryContainer = Color.White,
    secondary = StudioPurple,
    onSecondary = Color.White,
    tertiary = StudioNeonGreen,
    onTertiary = StudioObsidian,
    error = StudioLiveRed,
    onError = Color.White,
    background = StudioObsidian,
    onBackground = TextPrimary,
    surface = StudioDarkSurface,
    onSurface = TextPrimary,
    surfaceVariant = StudioSurfaceVariant,
    onSurfaceVariant = TextSecondary,
    outline = StudioCardBorder,
    outlineVariant = StudioCardBorder.copy(alpha = 0.5f)
)

@Composable
fun MyApplicationTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false, // Always enforce studio dark theme for broadcast immersion
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = StudioColorScheme,
        typography = Typography,
        content = content
    )
}
