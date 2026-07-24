package com.mobile.podcast.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val LightColors = lightColorScheme(
    primary = Indigo40,
    onPrimary = Slate90,
    secondary = OnLightSecondary,
    background = Slate90,
    onBackground = OnLightPrimary,
    surface = androidx.compose.ui.graphics.Color.White,
    onSurface = OnLightPrimary,
    onSurfaceVariant = OnLightSecondary,
)

private val DarkColors = darkColorScheme(
    primary = Indigo80,
    onPrimary = Indigo20,
    secondary = Slate90,
    background = Slate10,
    onBackground = Slate90,
    surface = Slate20,
    onSurface = Slate90,
)

@Composable
fun PodcastTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = if (darkTheme) DarkColors else LightColors,
        typography = AppTypography,
        content = content
    )
}
