package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.components.GlassCard
import com.example.viewmodel.AetherViewModel

@Composable
fun ShimmerButtonBackground(
    isGenerating: Boolean,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    if (isGenerating) {
        val shimmerColors = listOf(
            Color(0xFF0284C7),
            Color(0xFF38BDF8),
            Color(0xFF818CF8),
            Color(0xFF0284C7)
        )
        val transition = rememberInfiniteTransition(label = "shimmerTransition")
        val translateAnimation by transition.animateFloat(
            initialValue = 0f,
            targetValue = 1000f,
            animationSpec = infiniteRepeatable(
                animation = tween(durationMillis = 1000, easing = LinearEasing),
                repeatMode = RepeatMode.Restart
            ),
            label = "shimmerTranslate"
        )
        val brush = Brush.linearGradient(
            colors = shimmerColors,
            start = Offset(translateAnimation - 500f, translateAnimation - 500f),
            end = Offset(translateAnimation, translateAnimation)
        )
        Box(
            modifier = modifier
                .clip(RoundedCornerShape(10.dp))
                .background(brush),
            contentAlignment = Alignment.Center
        ) {
            content()
        }
    } else {
        content()
    }
}

@Composable
fun AutomationScreen(
    viewModel: AetherViewModel,
    modifier: Modifier = Modifier
) {
    val stepperState by viewModel.stepperState.collectAsState()
    val pipelineStatus by viewModel.pipelineStatus.collectAsState()

    var topicPrompt by remember { mutableStateOf("AI Video Automation Workflows") }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
            .testTag("automation_screen"),
        contentPadding = PaddingValues(top = 16.dp, bottom = 100.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Title Header
        item {
            Column {
                Text(
                    text = "AI Video Automation Flow",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = "Automated video pipeline: select media, generate AI metadata, and schedule.",
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }

        // Stepper Navigation Header
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                StepperHeaderItem(
                    stepNumber = 1,
                    title = "Media",
                    isSelected = stepperState.currentStep == 1,
                    isCompleted = stepperState.currentStep > 1,
                    onClick = { viewModel.setStepperStep(1) }
                )
                Box(modifier = Modifier.width(20.dp).height(2.dp).background(MaterialTheme.colorScheme.outline))
                StepperHeaderItem(
                    stepNumber = 2,
                    title = "AI Metadata",
                    isSelected = stepperState.currentStep == 2,
                    isCompleted = stepperState.currentStep > 2,
                    onClick = { viewModel.setStepperStep(2) }
                )
                Box(modifier = Modifier.width(20.dp).height(2.dp).background(MaterialTheme.colorScheme.outline))
                StepperHeaderItem(
                    stepNumber = 3,
                    title = "Schedule",
                    isSelected = stepperState.currentStep == 3,
                    isCompleted = stepperState.currentStep > 3,
                    onClick = { viewModel.setStepperStep(3) }
                )
            }
        }

        // STEP 1: MEDIA SELECTION
        if (stepperState.currentStep == 1) {
            item {
                GlassCard(
                    modifier = Modifier.fillMaxWidth(),
                    isGlowEnabled = true
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = "Step 1: Select Media File",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface,
                            modifier = Modifier.padding(bottom = 12.dp)
                        )

                        // Drag & Drop / Tap Picker Box
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(160.dp)
                                .clip(RoundedCornerShape(16.dp))
                                .background(MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.3f))
                                .border(1.5.dp, MaterialTheme.colorScheme.primary, RoundedCornerShape(16.dp))
                                .clickable {
                                    viewModel.updateMediaSelection(
                                        uri = "file://media/videos/ai_demo_2026.mp4",
                                        name = "ai_automation_demo_4k.mp4 (428 MB • 03:45)"
                                    )
                                }
                                .padding(16.dp)
                                .testTag("media_picker_box"),
                            contentAlignment = Alignment.Center
                        ) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Icon(
                                    imageVector = Icons.Filled.CloudUpload,
                                    contentDescription = "Upload Video",
                                    tint = MaterialTheme.colorScheme.primary,
                                    modifier = Modifier.size(48.dp)
                                )
                                Spacer(modifier = Modifier.height(8.dp))
                                Text(
                                    text = stepperState.selectedMediaName ?: "Drag & drop video here or tap to browse gallery",
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                                Text(
                                    text = "Supports MP4, MOV, ProRes up to 4K 60fps",
                                    fontSize = 11.sp,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        Button(
                            onClick = { viewModel.setStepperStep(2) },
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier.fillMaxWidth().height(48.dp)
                        ) {
                            Text("Next: Generate AI Metadata →", fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }

        // STEP 2: METADATA & AI GENERATOR
        if (stepperState.currentStep == 2) {
            item {
                GlassCard(
                    modifier = Modifier.fillMaxWidth(),
                    isGlowEnabled = true
                ) {
                    Column {
                        Text(
                            text = "Step 2: Metadata & AI Generator",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        // Topic Prompt for AI
                        Text(
                            text = "Topic for AI Title & Tag Generator:",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = MaterialTheme.colorScheme.onSurface
                        )

                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.fillMaxWidth().padding(top = 4.dp)
                        ) {
                            OutlinedTextField(
                                value = topicPrompt,
                                onValueChange = { topicPrompt = it },
                                singleLine = true,
                                shape = RoundedCornerShape(10.dp),
                                modifier = Modifier.weight(1f).testTag("ai_topic_input")
                            )

                            Spacer(modifier = Modifier.width(8.dp))

                            ShimmerButtonBackground(isGenerating = stepperState.isAiGenerating) {
                                Button(
                                    onClick = { viewModel.generateAiMetadata(topicPrompt) },
                                    enabled = !stepperState.isAiGenerating,
                                    shape = RoundedCornerShape(10.dp),
                                    colors = if (stepperState.isAiGenerating) ButtonDefaults.buttonColors(
                                        containerColor = Color.Transparent,
                                        disabledContainerColor = Color.Transparent
                                    ) else ButtonDefaults.buttonColors(),
                                    modifier = Modifier.testTag("ai_suggest_button")
                                ) {
                                    if (stepperState.isAiGenerating) {
                                        Row(
                                            verticalAlignment = Alignment.CenterVertically,
                                            modifier = Modifier.padding(horizontal = 4.dp)
                                        ) {
                                            Icon(
                                                imageVector = Icons.Filled.AutoAwesome,
                                                contentDescription = "Generating",
                                                tint = Color.White,
                                                modifier = Modifier.size(16.dp)
                                            )
                                            Spacer(modifier = Modifier.width(6.dp))
                                            Text(
                                                text = "AI Thinking...",
                                                fontSize = 11.sp,
                                                fontWeight = FontWeight.Bold,
                                                color = Color.White
                                            )
                                        }
                                    } else {
                                        Row(verticalAlignment = Alignment.CenterVertically) {
                                            Icon(Icons.Filled.AutoAwesome, contentDescription = null, modifier = Modifier.size(16.dp))
                                            Spacer(modifier = Modifier.width(4.dp))
                                            Text("AI Suggest", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                                        }
                                    }
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        // Video Title Field
                        OutlinedTextField(
                            value = stepperState.title,
                            onValueChange = { viewModel.updateMetadataFields(title = it) },
                            label = { Text("Video Title") },
                            shape = RoundedCornerShape(10.dp),
                            modifier = Modifier.fillMaxWidth().testTag("video_title_input")
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        // Video Description Field
                        OutlinedTextField(
                            value = stepperState.description,
                            onValueChange = { viewModel.updateMetadataFields(description = it) },
                            label = { Text("SEO Description") },
                            maxLines = 3,
                            shape = RoundedCornerShape(10.dp),
                            modifier = Modifier.fillMaxWidth().testTag("video_description_input")
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        // Video Tags Field
                        OutlinedTextField(
                            value = stepperState.tags,
                            onValueChange = { viewModel.updateMetadataFields(tags = it) },
                            label = { Text("Tags (comma separated)") },
                            shape = RoundedCornerShape(10.dp),
                            modifier = Modifier.fillMaxWidth().testTag("video_tags_input")
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        // Visibility Options
                        Text("Visibility Scope:", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurface)
                        Row(
                            modifier = Modifier.fillMaxWidth().padding(top = 6.dp),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            listOf("Public", "Unlisted", "Private").forEach { vis ->
                                FilterChip(
                                    selected = stepperState.visibility == vis,
                                    onClick = { viewModel.updateMetadataFields(visibility = vis) },
                                    label = { Text(vis) }
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            OutlinedButton(onClick = { viewModel.setStepperStep(1) }) {
                                Text("← Back")
                            }
                            Button(onClick = { viewModel.setStepperStep(3) }) {
                                Text("Next: Schedule →", fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }
            }
        }

        // STEP 3: SCHEDULING
        if (stepperState.currentStep == 3) {
            item {
                GlassCard(
                    modifier = Modifier.fillMaxWidth(),
                    isGlowEnabled = true
                ) {
                    Column {
                        Text(
                            text = "Step 3: Scheduling & Automation",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            OutlinedTextField(
                                value = stepperState.scheduledDate,
                                onValueChange = { viewModel.updateMetadataFields(scheduledDate = it) },
                                label = { Text("Publish Date") },
                                modifier = Modifier.weight(1f)
                            )

                            OutlinedTextField(
                                value = stepperState.scheduledTime,
                                onValueChange = { viewModel.updateMetadataFields(scheduledTime = it) },
                                label = { Text("Publish Time") },
                                modifier = Modifier.weight(1f)
                            )
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Filled.Sync, contentDescription = "Sync", tint = Color(0xFF34D399), modifier = Modifier.size(18.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "Time Zone Synced • UTC (Coordinated Universal Time)",
                                fontSize = 11.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }

                        Spacer(modifier = Modifier.height(20.dp))

                        Button(
                            onClick = { viewModel.scheduleCurrentVideo() },
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF34D399)),
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier.fillMaxWidth().height(50.dp).testTag("confirm_schedule_button")
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Filled.ScheduleSend, contentDescription = "Confirm Schedule", tint = Color.Black)
                                Spacer(modifier = Modifier.width(8.dp))
                                Text("Confirm & Deploy to Automation Pipeline", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = Color.Black)
                            }
                        }
                    }
                }
            }
        }

        // Live Automation Pipeline Tracker
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
                        Text(
                            text = "Automation Pipeline Tracker",
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )

                        Text(
                            text = "STATUS: ${pipelineStatus.uppercase()}",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Black,
                            color = Color(0xFF38BDF8)
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        PipelineStepBadge("Scheduled", active = pipelineStatus in listOf("Scheduled", "Processing", "Uploading", "Success"))
                        Box(modifier = Modifier.weight(1f).height(2.dp).background(Color(0x3338BDF8)))
                        PipelineStepBadge("Processing", active = pipelineStatus in listOf("Processing", "Uploading", "Success"))
                        Box(modifier = Modifier.weight(1f).height(2.dp).background(Color(0x3338BDF8)))
                        PipelineStepBadge("Uploading", active = pipelineStatus in listOf("Uploading", "Success"))
                        Box(modifier = Modifier.weight(1f).height(2.dp).background(Color(0x3338BDF8)))
                        PipelineStepBadge("Success", active = pipelineStatus == "Success")
                    }
                }
            }
        }
    }
}

@Composable
fun StepperHeaderItem(
    stepNumber: Int,
    title: String,
    isSelected: Boolean,
    isCompleted: Boolean,
    onClick: () -> Unit
) {
    val bg = when {
        isSelected -> MaterialTheme.colorScheme.primary
        isCompleted -> Color(0xFF34D399)
        else -> MaterialTheme.colorScheme.surfaceVariant
    }

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .clip(CircleShape)
            .clickable(onClick = onClick)
            .padding(horizontal = 8.dp, vertical = 4.dp)
    ) {
        Box(
            modifier = Modifier
                .size(24.dp)
                .clip(CircleShape)
                .background(bg),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "$stepNumber",
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
        }
        Spacer(modifier = Modifier.width(6.dp))
        Text(
            text = title,
            fontSize = 11.sp,
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
            color = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface
        )
    }
}

@Composable
fun PipelineStepBadge(name: String, active: Boolean) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Box(
            modifier = Modifier
                .size(20.dp)
                .clip(CircleShape)
                .background(if (active) Color(0xFF38BDF8) else Color(0x33CBD5E1)),
            contentAlignment = Alignment.Center
        ) {
            if (active) {
                Icon(Icons.Filled.Check, contentDescription = null, tint = Color.Black, modifier = Modifier.size(12.dp))
            }
        }
        Text(name, fontSize = 9.sp, color = if (active) Color(0xFF38BDF8) else MaterialTheme.colorScheme.onSurfaceVariant, modifier = Modifier.padding(top = 4.dp))
    }
}
