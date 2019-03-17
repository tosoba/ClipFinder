package com.example.db.model.videos

import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey

@Entity(tableName = "video_playlists")
data class VideoPlaylistDbModel(
        @PrimaryKey(autoGenerate = true)
        val id: Long?,

        val name: String
)