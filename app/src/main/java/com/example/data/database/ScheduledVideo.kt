package com.example.data.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "scheduled_videos")
data class ScheduledVideo(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val title: String,
    val description: String,
    val tags: String,
    val visibility: String, // Public, Unlisted, Private
    val scheduledTime: String,
    val scheduledDate: String,
    val status: String, // Scheduled, Processing, Uploading, Success, Paused
    val progress: Int = 0,
    val videoUri: String? = null,
    val thumbnailType: String = "AI Generated",
    val timestamp: Long = System.currentTimeMillis()
)
