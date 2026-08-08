package com.example.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColorScheme = darkColorScheme(
    primary = CyanGlow,
    onPrimary = DeepSpaceDark,
    primaryContainer = Color(0xFF1E293B),
    onPrimaryContainer = TextPrimaryDark,
    secondary = ElectricIndigo,
    tertiary = EmeraldLive,
    background = DeepSpaceDark,
    onBackground = TextPrimaryDark,
    surface = Color(0xFF0F172A),
    onSurface = TextPrimaryDark,
    surfaceVariant = Color(0xFF1E293B),
    onSurfaceVariant = TextSecondaryDark,
    outline = GlassBorderDark
)

private val LightColorScheme = lightColorScheme(
    primary = PrimaryBlueLight,
    onPrimary = Color.White,
    primaryContainer = Color(0xFFE0F2FE),
    onPrimaryContainer = TextPrimaryLight,
    secondary = SecondaryIndigoLight,
    tertiary = EmeraldLive,
    background = IceGlassLight,
    onBackground = TextPrimaryLight,
    surface = Color.White,
    onSurface = TextPrimaryLight,
    surfaceVariant = Color(0xFFF1F5F9),
    onSurfaceVariant = TextSecondaryLight,
    outline = GlassBorderLight
)

@Composable
fun AetherFlowTheme(
    darkTheme: Boolean = true,
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
