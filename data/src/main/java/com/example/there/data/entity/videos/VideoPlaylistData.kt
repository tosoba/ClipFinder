package com.example.there.data.entity.videos

import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey

@Entity(tableName = "video_playlists")
data class VideoPlaylistData(
        @PrimaryKey(autoGenerate = true)
        val id: Long?,

        val name: String
)