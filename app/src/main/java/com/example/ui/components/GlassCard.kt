package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun GlassCard(
    modifier: Modifier = Modifier,
    cornerRadius: Dp = 20.dp,
    onClick: (() -> Unit)? = null,
    isGlowEnabled: Boolean = false,
    content: @Composable BoxScope.() -> Unit
) {
    val isDark = isSystemInDarkTheme()

    val backgroundGradient = if (isDark) {
        Brush.linearGradient(
            colors = listOf(
                Color(0x331E293B),
                Color(0x1A0F172A),
                Color(0x2B334155)
            )
        )
    } else {
        Brush.linearGradient(
            colors = listOf(
                Color(0xD9FFFFFF),
                Color(0xB3F1F5F9),
                Color(0xE6FFFFFF)
            )
        )
    }

    val borderColor = if (isDark) {
        if (isGlowEnabled) Color(0x8038BDF8) else Color(0x3338BDF8)
    } else {
        if (isGlowEnabled) Color(0x990284C7) else Color(0x330284C7)
    }

    val shadowElevation = if (isGlowEnabled) 12.dp else 4.dp

    val shape = RoundedCornerShape(cornerRadius)

    var cardModifier = modifier
        .shadow(
            elevation = shadowElevation,
            shape = shape,
            ambientColor = if (isDark) Color(0xFF38BDF8) else Color(0xFF0284C7),
            spotColor = if (isDark) Color(0xFF818CF8) else Color(0xFF4338CA)
        )
        .clip(shape)
        .background(backgroundGradient)
        .border(width = 1.dp, color = borderColor, shape = shape)

    if (onClick != null) {
        cardModifier = cardModifier.clickable(onClick = onClick)
    }

    Box(
        modifier = cardModifier.padding(16.dp),
        content = content
    )
}
