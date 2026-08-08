package com.example.ui.screens

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AttachMoney
import androidx.compose.material.icons.filled.ShowChart
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.components.AdMobBannerPlaceholder
import com.example.ui.components.AdMobNativeInFeedPlaceholder
import com.example.ui.components.GlassCard
import com.example.viewmodel.AetherViewModel

@Composable
fun RevenueScreen(
    viewModel: AetherViewModel,
    modifier: Modifier = Modifier
) {
    val analytics by viewModel.analytics.collectAsState()
    val isDark = isSystemInDarkTheme()

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
            .testTag("revenue_screen"),
        contentPadding = PaddingValues(top = 16.dp, bottom = 100.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Title Header
        item {
            Column {
                Text(
                    text = "Revenue & Monetization Dashboard",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = "YouTube channel monetization stats & mobile ad network performance.",
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }

        // Channel Revenue Cards Grid
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                GlassCard(
                    modifier = Modifier.weight(1f),
                    isGlowEnabled = true
                ) {
                    Column {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Filled.AttachMoney, contentDescription = null, tint = Color(0xFF34D399), modifier = Modifier.size(18.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Est. Revenue", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        }
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(analytics.estimatedRevenue, fontSize = 20.sp, fontWeight = FontWeight.Black, color = Color(0xFF34D399))
                        Text(analytics.growthRate, fontSize = 10.sp, fontWeight = FontWeight.Bold, color = Color(0xFF34D399))
                    }
                }

                GlassCard(modifier = Modifier.weight(1f)) {
                    Column {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Filled.TrendingUp, contentDescription = null, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(18.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Channel RPM", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        }
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(analytics.rpm, fontSize = 20.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurface)
                        Text("Per 1,000 views", fontSize = 10.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                }
            }
        }

        // Playback CPM Card
        item {
            GlassCard(modifier = Modifier.fillMaxWidth()) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text("Playback-based CPM", fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        Text(analytics.cpm, fontSize = 22.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurface)
                    }

                    Box(
                        modifier = Modifier
                            .padding(8.dp)
                    ) {
                        Text(
                            text = "+14.2% YoY",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF34D399)
                        )
                    }
                }
            }
        }

        // 30-Day Views & Revenue Growth Chart
        item {
            GlassCard(
                modifier = Modifier.fillMaxWidth(),
                isGlowEnabled = true
            ) {
                Column {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Filled.ShowChart, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("30-Day Revenue & Views Growth", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurface)
                        }

                        Text("Live Sync", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = Color(0xFF38BDF8))
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Chart Canvas
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(140.dp)
                    ) {
                        Canvas(modifier = Modifier.fillMaxSize()) {
                            val w = size.width
                            val h = size.height

                            val path = Path().apply {
                                moveTo(0f, h * 0.8f)
                                cubicTo(w * 0.25f, h * 0.6f, w * 0.4f, h * 0.9f, w * 0.6f, h * 0.3f)
                                cubicTo(w * 0.75f, h * 0.1f, w * 0.9f, h * 0.4f, w, h * 0.15f)
                            }

                            val fillPath = Path().apply {
                                addPath(path)
                                lineTo(w, h)
                                lineTo(0f, h)
                                close()
                            }

                            // Gradient Fill
                            drawPath(
                                path = fillPath,
                                brush = Brush.verticalGradient(
                                    colors = listOf(
                                        Color(0xFF38BDF8).copy(alpha = 0.4f),
                                        Color.Transparent
                                    )
                                )
                            )

                            // Curve Stroke
                            drawPath(
                                path = path,
                                color = Color(0xFF38BDF8),
                                style = Stroke(width = 3.dp.toPx())
                            )
                        }
                    }
                }
            }
        }

        // App Monetization Section Header
        item {
            Text(
                text = "App Monetization & Mobile Ads Section",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.padding(top = 8.dp)
            )
        }

        // AdMob Adaptive Banner Placement
        item {
            AdMobBannerPlaceholder()
        }

        // AdMob Native In-Feed Placement
        item {
            AdMobNativeInFeedPlaceholder()
        }
    }
}
