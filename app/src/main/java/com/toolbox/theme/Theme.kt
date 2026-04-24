package com.toolbox.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val LightColorScheme = lightColorScheme(
    primary = BrandPrimary,
    onPrimary = White,
    secondary = AccentOrange,
    onSecondary = White,
    background = NeutralBackground,
    onBackground = NeutralBlack,
    surface = White,
    onSurface = NeutralBlack,
    error = DangerRed,
    onError = White,
)

private val DarkColorScheme = darkColorScheme(
    primary = BrandPrimary,
    onPrimary = White,
    secondary = AccentOrange,
    onSecondary = White,
    background = Color(0xFF1A1A1A),
    onBackground = Color(0xFFE0E0E0),
    surface = Color(0xFF2C2C2C),
    onSurface = Color(0xFFE0E0E0),
    error = DangerRed,
    onError = White,
)

@Composable
fun ToolBoxTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
