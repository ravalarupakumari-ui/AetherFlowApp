package com.example.ui.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CloudUpload
import androidx.compose.material.icons.filled.Error
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun CircularProgressWidget(
    percentage: Int,
    speedMbps: Double,
    remainingTimeText: String,
    videoTitle: String,
    isFailed: Boolean = false,
    errorMessage: String = "Network Reset (504)",
    onRetry: () -> Unit = {},
    onSimulateFail: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    val isDark = isSystemInDarkTheme()
    val animatedProgress by animateFloatAsState(
        targetValue = percentage / 100f,
        animationSpec = tween(durationMillis = 800),
        label = "circularProgress"
    )

    val trackColor = if (isDark) Color(0x33334155) else Color(0x33CBD5E1)
    val glowColorStart = if (isFailed) Color(0xFFF43F5E) else if (isDark) Color(0xFF38BDF8) else Color(0xFF0284C7)
    val glowColorEnd = if (isFailed) Color(0xFFFB7185) else if (isDark) Color(0xFF818CF8) else Color(0xFF6366F1)

    GlassCard(
        modifier = modifier.testTag("upload_progress_widget"),
        isGlowEnabled = true
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth().padding(bottom = 12.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = if (isFailed) Icons.Filled.Error else Icons.Filled.CloudUpload,
                        contentDescription = "Active Upload",
                        tint = glowColorStart,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = if (isFailed) "Upload Interrupted" else "Active Video Upload",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (isFailed) Color(0xFFF43F5E) else MaterialTheme.colorScheme.onSurface
                    )
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(12.dp))
                            .background(if (isFailed) Color(0x33F43F5E) else Color(0x2238BDF8))
                            .padding(horizontal = 8.dp, vertical = 4.dp)
                    ) {
                        Text(
                            text = if (isFailed) "STATUS: FAILED" else "LIVE PIPELINE",
                            fontSize = 10.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = glowColorStart
                        )
                    }

                    Spacer(modifier = Modifier.width(4.dp))

                    // Quick simulation trigger for testing error / retry state
                    IconButton(
                        onClick = { if (isFailed) onRetry() else onSimulateFail() },
                        modifier = Modifier.size(28.dp).testTag("simulate_fail_toggle")
                    ) {
                        Icon(
                            imageVector = if (isFailed) Icons.Filled.Refresh else Icons.Filled.Warning,
                            contentDescription = "Toggle Fail Test",
                            tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f),
                            modifier = Modifier.size(14.dp)
                        )
                    }
                }
            }

            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier.size(140.dp).padding(8.dp)
            ) {
                Canvas(modifier = Modifier.fillMaxSize()) {
                    val strokeWidth = 14.dp.toPx()

                    // Background Track
                    drawArc(
                        color = trackColor,
                        startAngle = 140f,
                        sweepAngle = 260f,
                        useCenter = false,
                        style = Stroke(width = strokeWidth, cap = StrokeCap.Round)
                    )

                    // Glowing Animated Arc
                    drawArc(
                        brush = Brush.sweepGradient(
                            colors = listOf(glowColorStart, glowColorEnd, glowColorStart)
                        ),
                        startAngle = 140f,
                        sweepAngle = 260f * animatedProgress,
                        useCenter = false,
                        style = Stroke(width = strokeWidth, cap = StrokeCap.Round)
                    )
                }

                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = if (isFailed) "ERR" else "$percentage%",
                        fontSize = 28.sp,
                        fontWeight = FontWeight.Black,
                        color = if (isFailed) Color(0xFFF43F5E) else MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = if (isFailed) "DROPPED" else "TRANSFERRING",
                        fontSize = 9.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (isFailed) Color(0xFFF43F5E) else MaterialTheme.colorScheme.onSurfaceVariant,
                        letterSpacing = 1.sp
                    )
                }
            }

            Text(
                text = videoTitle,
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.padding(top = 4.dp, bottom = 8.dp)
            )

            if (isFailed) {
                Text(
                    text = "Reason: $errorMessage",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color(0xFFF43F5E),
                    modifier = Modifier.padding(bottom = 12.dp)
                )

                Button(
                    onClick = onRetry,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFFF43F5E),
                        contentColor = Color.White
                    ),
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(42.dp)
                        .testTag("retry_upload_button")
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Filled.Refresh,
                            contentDescription = "Retry Upload",
                            tint = Color.White,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Retry Upload Pipeline",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            } else {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceAround
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Filled.Speed,
                            contentDescription = "Transfer Speed",
                            tint = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "$speedMbps Mbps",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Medium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Filled.Timer,
                            contentDescription = "Remaining Time",
                            tint = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = remainingTimeText,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Medium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
        }
    }
}
