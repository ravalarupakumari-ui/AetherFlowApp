package com.example.data.database

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface ScheduledVideoDao {
    @Query("SELECT * FROM scheduled_videos ORDER BY timestamp DESC")
    fun getAllVideos(): Flow<List<ScheduledVideo>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertVideo(video: ScheduledVideo): Long

    @Update
    suspend fun updateVideo(video: ScheduledVideo)

    @Query("DELETE FROM scheduled_videos WHERE id = :id")
    suspend fun deleteVideoById(id: Long)

    @Query("UPDATE scheduled_videos SET status = :status, progress = :progress WHERE id = :id")
    suspend fun updateStatusAndProgress(id: Long, status: String, progress: Int)
}
