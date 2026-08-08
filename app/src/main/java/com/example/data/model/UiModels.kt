package com.example.data.model

data class ChannelAnalytics(
    val viewsCount: String = "14.2K",
    val subscriberCount: String = "1.1K",
    val watchTimeHours: String = "342.5 hrs",
    val estimatedRevenue: String = "$1,420.50",
    val rpm: String = "$4.10",
    val cpm: String = "$8.60",
    val growthRate: String = "+18.4%"
)

data class IntegrationAccount(
    val id: String,
    val platformName: String,
    val handle: String,
    val isConnected: Boolean,
    val requiredScopes: List<String>,
    val statusLabel: String,
    val iconType: String
)

data class UploadProgress(
    val videoTitle: String = "AI Tech Trends 2026",
    val percentage: Int = 52,
    val speedMbps: Double = 8.5,
    val remainingTimeSeconds: Int = 252,
    val isUploading: Boolean = true,
    val isFailed: Boolean = false,
    val errorMessage: String = "Network Timeout (504)"
)
