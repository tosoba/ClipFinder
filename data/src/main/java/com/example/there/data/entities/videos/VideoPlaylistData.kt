package com.example.there.data.entities.videos

import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey

@Entity(tableName = "video_playlists")
data class VideoPlaylistData(
        val name: String
) {
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0L
}