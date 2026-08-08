package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.components.GlassCard
import com.example.viewmodel.AetherViewModel

data class EcosystemApp(
    val id: String,
    val name: String,
    val description: String,
    val status: String, // Active, Coming Soon, Connected
    val iconName: String
)

@Composable
fun AppsHubScreen(
    viewModel: AetherViewModel,
    modifier: Modifier = Modifier
) {
    var selectedAppForModal by remember { mutableStateOf<EcosystemApp?>(null) }

    val apps = listOf(
        EcosystemApp("youtube", "YouTube Studio", "Full auto-upload, shorts scheduler & metadata AI", "Active / Connected", "youtube"),
        EcosystemApp("tiktok", "TikTok For Creators", "Auto-publish short video clips with AI hashtags", "Coming Soon", "tiktok"),
        EcosystemApp("instagram", "Instagram Reels", "Auto-sync reels & story broadcast automation", "Coming Soon", "instagram"),
        EcosystemApp("xshorts", "X (Twitter) Video", "Publish viral video posts & thread embeds", "Coming Soon", "x"),
        EcosystemApp("twitch", "Twitch VOD Sync", "Auto-clip VOD highlights & post directly", "Coming Soon", "twitch"),
        EcosystemApp("linkedin", "LinkedIn Video", "Professional video updates & newsletter embeds", "Coming Soon", "linkedin")
    )

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
            .testTag("apps_hub_screen")
    ) {
        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Apps & Ecosystem Hub",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface
        )
        Text(
            text = "Multi-platform distribution ecosystem & upcoming API connectors.",
            fontSize = 12.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Spacer(modifier = Modifier.height(16.dp))

        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            contentPadding = PaddingValues(bottom = 100.dp),
            modifier = Modifier.fillMaxSize()
        ) {
            items(apps, key = { it.id }) { app ->
                EcosystemCard(
                    app = app,
                    onClick = { selectedAppForModal = app }
                )
            }
        }
    }

    // Detail Modal Dialog
    selectedAppForModal?.let { app ->
        AlertDialog(
            onDismissRequest = { selectedAppForModal = null },
            title = {
                Text(app.name, fontWeight = FontWeight.Bold)
            },
            text = {
                Column {
                    Text(app.description, fontSize = 13.sp)
                    Spacer(modifier = Modifier.height(12.dp))
                    Text("Status: ${app.status}", fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
                }
            },
            confirmButton = {
                Button(onClick = { selectedAppForModal = null }) {
                    Text("Close")
                }
            }
        )
    }
}

@Composable
fun EcosystemCard(
    app: EcosystemApp,
    onClick: () -> Unit
) {
    val isActive = app.status.contains("Active")

    GlassCard(
        modifier = Modifier
            .fillMaxWidth()
            .height(180.dp),
        onClick = onClick,
        isGlowEnabled = isActive
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .clip(RoundedCornerShape(10.dp))
                        .background(if (isActive) Color(0x3338BDF8) else Color(0x22CBD5E1)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = if (isActive) Icons.Filled.VideoLibrary else Icons.Filled.Cloud,
                        contentDescription = app.name,
                        tint = if (isActive) Color(0xFF38BDF8) else MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(10.dp))
                        .background(
                            if (isActive) Color(0x3334D399) else Color(0x22FBBF24)
                        )
                        .border(
                            1.dp,
                            if (isActive) Color(0xFF34D399) else Color(0xFFFBBF24),
                            RoundedCornerShape(10.dp)
                        )
                        .padding(horizontal = 6.dp, vertical = 2.dp)
                ) {
                    Text(
                        text = if (isActive) "Active" else "Soon",
                        fontSize = 9.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (isActive) Color(0xFF34D399) else Color(0xFFFBBF24)
                    )
                }
            }

            Column {
                Text(
                    text = app.name,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = app.description,
                    fontSize = 10.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 2
                )
            }

            Button(
                onClick = onClick,
                shape = RoundedCornerShape(8.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (isActive) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surfaceVariant
                ),
                modifier = Modifier.fillMaxWidth().height(32.dp)
            ) {
                Text(
                    text = if (isActive) "Manage" else "Join Waitlist",
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}
