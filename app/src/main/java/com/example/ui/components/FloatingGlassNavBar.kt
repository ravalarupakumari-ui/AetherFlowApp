package com.example.ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

enum class NavDestination(val route: String, val title: String, val activeIcon: ImageVector, val inactiveIcon: ImageVector) {
    DASHBOARD("dashboard", "Home", Icons.Filled.Home, Icons.Outlined.Home),
    AUTOMATION("automation", "Automation", Icons.Filled.AutoAwesome, Icons.Outlined.AutoAwesome),
    INTEGRATIONS("integrations", "Connect", Icons.Filled.VpnKey, Icons.Outlined.VpnKey),
    APPS("apps", "Ecosystem", Icons.Filled.GridView, Icons.Outlined.GridView),
    REVENUE("revenue", "Revenue", Icons.Filled.MonetizationOn, Icons.Outlined.MonetizationOn),
    PROFILE("profile", "Profile", Icons.Filled.Person, Icons.Outlined.Person)
}

@Composable
fun FloatingGlassNavBar(
    currentRoute: String,
    onNavigate: (NavDestination) -> Unit,
    modifier: Modifier = Modifier
) {
    val isDark = isSystemInDarkTheme()

    val backgroundGradient = if (isDark) {
        Brush.linearGradient(
            colors = listOf(
                Color(0xE60F172A),
                Color(0xCC1E293B),
                Color(0xF2090D16)
            )
        )
    } else {
        Brush.linearGradient(
            colors = listOf(
                Color(0xF2FFFFFF),
                Color(0xE6F1F5F9),
                Color(0xF8FFFFFF)
            )
        )
    }

    val borderColor = if (isDark) Color(0x4038BDF8) else Color(0x400284C7)

    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp, vertical = 12.dp),
        contentAlignment = Alignment.Center
    ) {
        Row(
            modifier = Modifier
                .shadow(
                    elevation = 16.dp,
                    shape = RoundedCornerShape(32.dp),
                    ambientColor = if (isDark) Color(0xFF38BDF8) else Color(0xFF0284C7)
                )
                .clip(RoundedCornerShape(32.dp))
                .background(backgroundGradient)
                .border(1.dp, borderColor, RoundedCornerShape(32.dp))
                .padding(horizontal = 8.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            NavDestination.entries.forEach { destination ->
                val isSelected = currentRoute == destination.route

                val iconColor by animateColorAsState(
                    targetValue = if (isSelected) {
                        if (isDark) Color(0xFF38BDF8) else Color(0xFF0284C7)
                    } else {
                        if (isDark) Color(0xFF94A3B8) else Color(0xFF64748B)
                    },
                    animationSpec = spring(stiffness = Spring.StiffnessLow),
                    label = "navIconColor"
                )

                val pillBackground = if (isSelected) {
                    if (isDark) Color(0x3338BDF8) else Color(0x260284C7)
                } else {
                    Color.Transparent
                }

                Box(
                    modifier = Modifier
                        .testTag("nav_item_${destination.route}")
                        .clip(RoundedCornerShape(20.dp))
                        .background(pillBackground)
                        .clickable { onNavigate(destination) }
                        .padding(horizontal = 10.dp, vertical = 8.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Icon(
                            imageVector = if (isSelected) destination.activeIcon else destination.inactiveIcon,
                            contentDescription = destination.title,
                            tint = iconColor,
                            modifier = Modifier.size(22.dp)
                        )

                        Text(
                            text = destination.title,
                            fontSize = 10.sp,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                            color = iconColor,
                            modifier = Modifier.padding(top = 2.dp)
                        )
                    }
                }
            }
        }
    }
}
