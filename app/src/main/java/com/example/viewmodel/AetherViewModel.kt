package com.example.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.database.AppDatabase
import com.example.data.database.ScheduledVideo
import com.example.data.model.ChannelAnalytics
import com.example.data.model.IntegrationAccount
import com.example.data.model.UploadProgress
import com.example.data.network.GeminiTitleGenerator
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

data class AutomationStepperState(
    val currentStep: Int = 1,
    val selectedMediaUri: String? = null,
    val selectedMediaName: String? = null,
    val title: String = "",
    val description: String = "",
    val tags: String = "",
    val visibility: String = "Public",
    val thumbnailType: String = "AI Generated",
    val scheduledDate: String = "2026-08-15",
    val scheduledTime: String = "18:00 UTC",
    val isAiGenerating: Boolean = false
)

class AetherViewModel(application: Application) : AndroidViewModel(application) {

    private val videoDao = AppDatabase.getDatabase(application).scheduledVideoDao()

    // Auth State
    private val _isLoggedIn = MutableStateFlow(true)
    val isLoggedIn: StateFlow<Boolean> = _isLoggedIn.asStateFlow()

    private val _userEmail = MutableStateFlow("creator@aetherflow.ai")
    val userEmail: StateFlow<String> = _userEmail.asStateFlow()

    // Navigation State
    private val _currentRoute = MutableStateFlow("dashboard")
    val currentRoute: StateFlow<String> = _currentRoute.asStateFlow()

    // Theme Mode
    private val _isDarkTheme = MutableStateFlow(true)
    val isDarkTheme: StateFlow<Boolean> = _isDarkTheme.asStateFlow()

    // Automation Active / Paused Toggle
    private val _isAutomationActive = MutableStateFlow(true)
    val isAutomationActive: StateFlow<Boolean> = _isAutomationActive.asStateFlow()

    // Storage Gauge (Used GB / Total GB)
    val usedStorageGB = 390
    val totalStorageGB = 500

    // Channel Analytics
    private val _analytics = MutableStateFlow(ChannelAnalytics())
    val analytics: StateFlow<ChannelAnalytics> = _analytics.asStateFlow()

    // Upload Progress Widget State
    private val _uploadProgress = MutableStateFlow(
        UploadProgress(
            videoTitle = "AI Automation Workflows 2026",
            percentage = 68,
            speedMbps = 12.4,
            remainingTimeSeconds = 115,
            isUploading = true
        )
    )
    val uploadProgress: StateFlow<UploadProgress> = _uploadProgress.asStateFlow()

    // Scheduled Videos from Room DB
    val scheduledVideos: StateFlow<List<ScheduledVideo>> = videoDao.getAllVideos()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    // Integrations & Permissions State
    private val _integrations = MutableStateFlow(
        listOf(
            IntegrationAccount(
                id = "youtube",
                platformName = "YouTube Channel API",
                handle = "Creator Studio Labs • Live",
                isConnected = true,
                requiredScopes = listOf("https://www.googleapis.com/auth/youtube.upload", "https://www.googleapis.com/auth/youtube.readonly"),
                statusLabel = "Connected",
                iconType = "youtube"
            ),
            IntegrationAccount(
                id = "gallery",
                platformName = "Device Media & Gallery Access",
                handle = "Read/Select Local Videos & Thumbnails",
                isConnected = true,
                requiredScopes = listOf("android.permission.READ_MEDIA_VIDEO", "android.permission.READ_MEDIA_IMAGES"),
                statusLabel = "Permission Granted",
                iconType = "gallery"
            ),
            IntegrationAccount(
                id = "tiktok",
                platformName = "TikTok Content API",
                handle = "@aether_flow_official",
                isConnected = false,
                requiredScopes = listOf("user.info.basic", "video.upload"),
                statusLabel = "Pending Setup",
                iconType = "tiktok"
            ),
            IntegrationAccount(
                id = "instagram",
                platformName = "Instagram Reels Publishing",
                handle = "@aether.flow.ai",
                isConnected = false,
                requiredScopes = listOf("instagram_basic", "instagram_content_publish"),
                statusLabel = "Pending Setup",
                iconType = "instagram"
            )
        )
    )
    val integrations: StateFlow<List<IntegrationAccount>> = _integrations.asStateFlow()

    // Automation Stepper Flow State
    private val _stepperState = MutableStateFlow(AutomationStepperState())
    val stepperState: StateFlow<AutomationStepperState> = _stepperState.asStateFlow()

    // Pipeline Tracker Status
    private val _pipelineStatus = MutableStateFlow("Scheduled") // Scheduled -> Processing -> Uploading -> Success
    val pipelineStatus: StateFlow<String> = _pipelineStatus.asStateFlow()

    init {
        // Seed default sample videos if DB is empty
        viewModelScope.launch {
            scheduledVideos.collectLatest { list ->
                if (list.isEmpty()) {
                    seedDefaultVideos()
                }
            }
        }

        // Simulate periodic live upload percentage updates
        startProgressSimulation()
    }

    private suspend fun seedDefaultVideos() {
        val sample1 = ScheduledVideo(
            title = "How Gemini 3.5 Works: Complete Developer Breakdown",
            description = "In-depth video exploring liquid glass UI, coroutine state management, and real-time AI automation pipelines.",
            tags = "Gemini, Android, JetpackCompose, Kotlin, AI",
            visibility = "Public",
            scheduledTime = "19:30 UTC",
            scheduledDate = "2026-08-10",
            status = "Scheduled",
            progress = 0
        )

        val sample2 = ScheduledVideo(
            title = "10 AI Tools Every Tech Creator Needs in 2026",
            description = "Discover the top AI tools for automated video editing, thumbnail generation, and YouTube SEO.",
            tags = "AITools, Productivity, YouTube, CreatorStudio",
            visibility = "Unlisted",
            scheduledTime = "14:00 UTC",
            scheduledDate = "2026-08-12",
            status = "Processing",
            progress = 45
        )

        videoDao.insertVideo(sample1)
        videoDao.insertVideo(sample2)
    }

    private fun startProgressSimulation() {
        viewModelScope.launch {
            while (true) {
                delay(3000)
                val current = _uploadProgress.value
                if (current.isUploading) {
                    val newPct = if (current.percentage >= 98) 15 else current.percentage + 4
                    val newTime = (100 - newPct) * 3
                    _uploadProgress.value = current.copy(
                        percentage = newPct,
                        remainingTimeSeconds = newTime,
                        speedMbps = (8.0 + (Math.random() * 6.0)).let { Math.round(it * 10) / 10.0 }
                    )
                }
            }
        }
    }

    fun toggleTheme() {
        _isDarkTheme.value = !_isDarkTheme.value
    }

    fun toggleAutomationActive() {
        _isAutomationActive.value = !_isAutomationActive.value
    }

    fun setRoute(route: String) {
        _currentRoute.value = route
    }

    fun login(email: String) {
        _userEmail.value = if (email.isBlank()) "creator@aetherflow.ai" else email
        _isLoggedIn.value = true
        _currentRoute.value = "dashboard"
    }

    fun logout() {
        _isLoggedIn.value = false
        _currentRoute.value = "login"
    }

    fun toggleIntegrationConnection(id: String) {
        _integrations.value = _integrations.value.map { account ->
            if (account.id == id) {
                val newConn = !account.isConnected
                account.copy(
                    isConnected = newConn,
                    statusLabel = if (newConn) "Connected" else "Pending Setup"
                )
            } else account
        }
    }

    // Stepper methods
    fun setStepperStep(step: Int) {
        _stepperState.value = _stepperState.value.copy(currentStep = step)
    }

    fun updateMediaSelection(uri: String, name: String) {
        _stepperState.value = _stepperState.value.copy(
            selectedMediaUri = uri,
            selectedMediaName = name
        )
    }

    fun updateMetadataFields(
        title: String? = null,
        description: String? = null,
        tags: String? = null,
        visibility: String? = null,
        scheduledDate: String? = null,
        scheduledTime: String? = null
    ) {
        val current = _stepperState.value
        _stepperState.value = current.copy(
            title = title ?: current.title,
            description = description ?: current.description,
            tags = tags ?: current.tags,
            visibility = visibility ?: current.visibility,
            scheduledDate = scheduledDate ?: current.scheduledDate,
            scheduledTime = scheduledTime ?: current.scheduledTime
        )
    }

    fun generateAiMetadata(topicPrompt: String) {
        viewModelScope.launch {
            _stepperState.value = _stepperState.value.copy(isAiGenerating = true)
            val generated = GeminiTitleGenerator.generateMetadata(topicPrompt)
            _stepperState.value = _stepperState.value.copy(
                title = generated.title,
                description = generated.description,
                tags = generated.tags,
                isAiGenerating = false
            )
        }
    }

    fun scheduleCurrentVideo() {
        viewModelScope.launch {
            val state = _stepperState.value
            val videoTitle = if (state.title.isBlank()) "New AI Video Automation" else state.title
            val newVideo = ScheduledVideo(
                title = videoTitle,
                description = if (state.description.isBlank()) "Automated liquid glass workflow publication." else state.description,
                tags = if (state.tags.isBlank()) "AI, Automation, YouTube" else state.tags,
                visibility = state.visibility,
                scheduledTime = state.scheduledTime,
                scheduledDate = state.scheduledDate,
                status = "Scheduled",
                progress = 0
            )

            videoDao.insertVideo(newVideo)
            _pipelineStatus.value = "Scheduled"

            // Reset stepper state and go to dashboard
            _stepperState.value = AutomationStepperState()
            _currentRoute.value = "dashboard"

            // Simulate Pipeline transition
            simulatePipelineExecution()
        }
    }

    fun deleteVideo(id: Long) {
        viewModelScope.launch {
            videoDao.deleteVideoById(id)
        }
    }

    fun retryUpload() {
        val current = _uploadProgress.value
        _uploadProgress.value = current.copy(
            isFailed = false,
            isUploading = true,
            percentage = if (current.percentage < 20) 25 else current.percentage,
            speedMbps = 14.2,
            remainingTimeSeconds = 90
        )
    }

    fun simulateUploadFailure() {
        val current = _uploadProgress.value
        _uploadProgress.value = current.copy(
            isFailed = true,
            isUploading = false,
            speedMbps = 0.0,
            errorMessage = "Connection Interrupted (SSL Reset)"
        )
    }

    fun moveVideoUp(video: ScheduledVideo) {
        viewModelScope.launch {
            val list = scheduledVideos.value.toMutableList()
            val index = list.indexOfFirst { it.id == video.id }
            if (index > 0) {
                val temp = list[index]
                list[index] = list[index - 1]
                list[index - 1] = temp
                // Re-insert or update DB order if needed
            }
        }
    }

    fun moveVideoDown(video: ScheduledVideo) {
        viewModelScope.launch {
            val list = scheduledVideos.value.toMutableList()
            val index = list.indexOfFirst { it.id == video.id }
            if (index >= 0 && index < list.size - 1) {
                val temp = list[index]
                list[index] = list[index + 1]
                list[index + 1] = temp
            }
        }
    }

    fun triggerVideoUploadNow(video: ScheduledVideo) {
        viewModelScope.launch {
            videoDao.updateStatusAndProgress(video.id, "Uploading", 15)
            _pipelineStatus.value = "Uploading"
            _uploadProgress.value = _uploadProgress.value.copy(
                videoTitle = video.title,
                percentage = 15,
                isUploading = true,
                isFailed = false
            )
            _currentRoute.value = "dashboard"
        }
    }

    private fun simulatePipelineExecution() {
        viewModelScope.launch {
            delay(2000)
            _pipelineStatus.value = "Processing"
            delay(2000)
            _pipelineStatus.value = "Uploading"
            delay(3000)
            _pipelineStatus.value = "Success"
        }
    }
}
