package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.database.ScheduledVideo
import com.example.ui.components.CircularProgressWidget
import com.example.ui.components.GlassCard
import com.example.viewmodel.AetherViewModel

@Composable
fun DashboardScreen(
    viewModel: AetherViewModel,
    modifier: Modifier = Modifier
) {
    val isDark by viewModel.isDarkTheme.collectAsState()
    val userEmail by viewModel.userEmail.collectAsState()
    val isAutomationActive by viewModel.isAutomationActive.collectAsState()
    val analytics by viewModel.analytics.collectAsState()
    val uploadProgress by viewModel.uploadProgress.collectAsState()
    val scheduledVideos by viewModel.scheduledVideos.collectAsState()

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
            .testTag("dashboard_screen"),
        contentPadding = PaddingValues(top = 16.dp, bottom = 100.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Top Header
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(44.dp)
                            .clip(CircleShape)
                            .background(MaterialTheme.colorScheme.primaryContainer)
                            .border(1.5.dp, MaterialTheme.colorScheme.primary, CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = userEmail.take(1).uppercase(),
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }

                    Spacer(modifier = Modifier.width(12.dp))

                    Column {
                        Text(
                            text = "Aether Workspace",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Text(
                            text = userEmail,
                            fontSize = 11.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    // Connected Channel Badge
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(20.dp))
                            .background(Color(0x3334D399))
                            .border(1.dp, Color(0xFF34D399), RoundedCornerShape(20.dp))
                            .padding(horizontal = 10.dp, vertical = 6.dp)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(8.dp)
                                    .clip(CircleShape)
                                    .background(Color(0xFF34D399))
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "Creator Studio • Live",
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF34D399)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.width(8.dp))

                    // Theme Switcher Button
                    IconButton(
                        onClick = { viewModel.toggleTheme() },
                        modifier = Modifier.testTag("theme_switch_toggle")
                    ) {
                        Icon(
                            imageVector = if (isDark) Icons.Filled.LightMode else Icons.Filled.DarkMode,
                            contentDescription = "Toggle Theme",
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                }
            }
        }

        // Status Cards Grid
        item {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    // Upload Queue Status
                    GlassCard(modifier = Modifier.weight(1f)) {
                        Column {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Filled.CloudQueue, contentDescription = "Queue", tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(18.dp))
                                Spacer(modifier = Modifier.width(6.dp))
                                Text("Upload Queue", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                            }
                            Spacer(modifier = Modifier.height(6.dp))
                            Text("${scheduledVideos.size} Videos", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurface)
                            Text("Auto Scheduled", fontSize = 10.sp, color = Color(0xFF34D399))
                        }
                    }

                    // Automation Active/Paused Toggle
                    GlassCard(
                        modifier = Modifier
                            .weight(1f)
                            .clickable { viewModel.toggleAutomationActive() }
                            .testTag("automation_status_toggle")
                    ) {
                        Column {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween,
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(Icons.Filled.AutoMode, contentDescription = "Automation", tint = if (isAutomationActive) Color(0xFF34D399) else Color(0xFFFBBF24), modifier = Modifier.size(18.dp))
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text("Automation", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                }
                                Switch(
                                    checked = isAutomationActive,
                                    onCheckedChange = { viewModel.toggleAutomationActive() },
                                    modifier = Modifier.scale(0.7f)
                                )
                            }
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = if (isAutomationActive) "ACTIVE" else "PAUSED",
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Black,
                                color = if (isAutomationActive) Color(0xFF34D399) else Color(0xFFFBBF24)
                            )
                        }
                    }
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    // Remaining Storage Gauge
                    GlassCard(modifier = Modifier.weight(1f)) {
                        Column {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Filled.Storage, contentDescription = "Storage", tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(18.dp))
                                Spacer(modifier = Modifier.width(6.dp))
                                Text("Storage Gauge", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                            }
                            Spacer(modifier = Modifier.height(6.dp))
                            Text("${viewModel.usedStorageGB} / ${viewModel.totalStorageGB} GB", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurface)
                            Spacer(modifier = Modifier.height(6.dp))
                            LinearProgressIndicator(
                                progress = viewModel.usedStorageGB.toFloat() / viewModel.totalStorageGB.toFloat(),
                                modifier = Modifier.fillMaxWidth().height(6.dp).clip(RoundedCornerShape(3.dp)),
                                color = MaterialTheme.colorScheme.primary,
                                trackColor = Color(0x33CBD5E1)
                            )
                            Text("78% Used", fontSize = 9.sp, color = MaterialTheme.colorScheme.onSurfaceVariant, modifier = Modifier.padding(top = 4.dp))
                        }
                    }

                    // Quick Channel Analytics
                    GlassCard(modifier = Modifier.weight(1f)) {
                        Column {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Filled.Analytics, contentDescription = "Analytics", tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(18.dp))
                                Spacer(modifier = Modifier.width(6.dp))
                                Text("Channel Stats", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                            }
                            Spacer(modifier = Modifier.height(6.dp))
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Column {
                                    Text(analytics.viewsCount, fontSize = 15.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurface)
                                    Text("Views", fontSize = 10.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                }
                                Column {
                                    Text(analytics.subscriberCount, fontSize = 15.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurface)
                                    Text("Subscribers", fontSize = 10.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                }
                            }
                        }
                    }
                }
            }
        }

        // Active Upload Progress Widget
        item {
            CircularProgressWidget(
                percentage = uploadProgress.percentage,
                speedMbps = uploadProgress.speedMbps,
                remainingTimeText = "${uploadProgress.remainingTimeSeconds / 60}m ${uploadProgress.remainingTimeSeconds % 60}s",
                videoTitle = uploadProgress.videoTitle,
                isFailed = uploadProgress.isFailed,
                errorMessage = uploadProgress.errorMessage,
                onRetry = { viewModel.retryUpload() },
                onSimulateFail = { viewModel.simulateUploadFailure() }
            )
        }

        // Scheduled Videos Header
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Scheduled Videos Pipeline",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )

                TextButton(onClick = { viewModel.setRoute("automation") }) {
                    Text("+ New Automation", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                }
            }
        }

        // Scheduled Videos List
        items(scheduledVideos, key = { it.id }) { video ->
            ScheduledVideoCard(
                video = video,
                onDelete = { viewModel.deleteVideo(video.id) },
                onUploadNow = { viewModel.triggerVideoUploadNow(video) },
                onMoveUp = { viewModel.moveVideoUp(video) },
                onMoveDown = { viewModel.moveVideoDown(video) }
            )
        }
    }
}

@Composable
fun ScheduledVideoCard(
    video: ScheduledVideo,
    onDelete: () -> Unit,
    onUploadNow: () -> Unit,
    onMoveUp: () -> Unit = {},
    onMoveDown: () -> Unit = {}
) {
    GlassCard(
        modifier = Modifier.fillMaxWidth(),
        isGlowEnabled = video.status == "Uploading"
    ) {
        Column {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Row(
                    modifier = Modifier.weight(1f),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Touch Drag Handle for manual reordering
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier
                            .padding(end = 8.dp)
                            .testTag("drag_handle_${video.id}")
                    ) {
                        Icon(
                            imageVector = Icons.Filled.DragHandle,
                            contentDescription = "Drag Handle Reorder",
                            tint = Color.White.copy(alpha = 0.7f),
                            modifier = Modifier.size(20.dp)
                        )
                        Row {
                            IconButton(
                                onClick = onMoveUp,
                                modifier = Modifier.size(18.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Filled.KeyboardArrowUp,
                                    contentDescription = "Move Up",
                                    tint = MaterialTheme.colorScheme.primary,
                                    modifier = Modifier.size(14.dp)
                                )
                            }
                            IconButton(
                                onClick = onMoveDown,
                                modifier = Modifier.size(18.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Filled.KeyboardArrowDown,
                                    contentDescription = "Move Down",
                                    tint = MaterialTheme.colorScheme.primary,
                                    modifier = Modifier.size(14.dp)
                                )
                            }
                        }
                    }

                    // Video Thumbnail Placeholder
                    Box(
                        modifier = Modifier
                            .size(56.dp, 42.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .background(MaterialTheme.colorScheme.primaryContainer),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Filled.VideoLibrary,
                            contentDescription = "Thumbnail",
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(22.dp)
                        )
                    }

                    Spacer(modifier = Modifier.width(10.dp))

                    Column {
                        // Title set to full white (#FFFFFF) for high contrast
                        Text(
                            text = video.title,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White,
                            maxLines = 2
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Filled.Schedule,
                                contentDescription = "Time",
                                tint = MaterialTheme.colorScheme.onSurfaceVariant,
                                modifier = Modifier.size(12.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "${video.scheduledDate} • ${video.scheduledTime}",
                                fontSize = 10.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }

                // Status Badge
                val badgeColor = when (video.status) {
                    "Uploading" -> Color(0xFF38BDF8)
                    "Processing" -> Color(0xFFFBBF24)
                    "Success" -> Color(0xFF34D399)
                    else -> Color(0xFF818CF8)
                }

                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(12.dp))
                        .background(badgeColor.copy(alpha = 0.2f))
                        .border(1.dp, badgeColor, RoundedCornerShape(12.dp))
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                ) {
                    Text(
                        text = video.status.uppercase(),
                        fontSize = 9.sp,
                        fontWeight = FontWeight.Bold,
                        color = badgeColor
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Action Buttons
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = onUploadNow,
                    modifier = Modifier.size(32.dp)
                ) {
                    Icon(
                        imageVector = Icons.Filled.Upload,
                        contentDescription = "Upload Now",
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(18.dp)
                    )
                }

                IconButton(
                    onClick = onDelete,
                    modifier = Modifier.size(32.dp)
                ) {
                    Icon(
                        imageVector = Icons.Filled.DeleteOutline,
                        contentDescription = "Delete",
                        tint = Color(0xFFF43F5E),
                        modifier = Modifier.size(18.dp)
                    )
                }
            }
        }
    }
}

// Scale helper modifier extension
fun Modifier.scale(scale: Float) = this.then(
    Modifier.padding((10 * (1f - scale)).dp)
)
