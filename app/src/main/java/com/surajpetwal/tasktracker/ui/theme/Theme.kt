package com.surajpetwal.tasktracker.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

// Modern Dashboard Color Scheme
private val DarkColorScheme = darkColorScheme(
    primary = Color(0xFF6A3D),
    onPrimary = Color.White,
    primaryContainer = Color(0xFF8A65),
    onPrimaryContainer = Color.White,
    secondary = Color(0xFFFFCCBC),
    onSecondary = Color(0xFF0F172A),
    background = Color(0xFF0F172A),
    onBackground = Color.White,
    surface = Color(0x4DFFFFFF), // Glass surface 30% transparent
    onSurface = Color.White,
    error = Color(0xFFF44336),
    onError = Color.White
)

private val LightColorScheme = lightColorScheme(
    primary = Color(0xFF6A3D),
    onPrimary = Color.White,
    primaryContainer = Color(0xFFFFCCBC),
    onPrimaryContainer = Color(0xFF0F172A),
    secondary = Color(0xFFFF9800),
    onSecondary = Color.White,
    background = Color(0xFFE0F7FA), // Light peach
    onBackground = Color(0xFF0F172A),
    surface = Color(0xCCFFFFFF),
    onSurface = Color(0xFF0F172A),
    error = Color(0xFFF44336),
    onError = Color.White
)

@Composable
fun TaskTrackerTheme(
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
