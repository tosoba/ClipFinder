package com.example.db.model.videos

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "video_playlists")
data class VideoPlaylistDbModel(
    @PrimaryKey(autoGenerate = true)
    val id: Long?,

    val name: String
)
