package com.example.there.data.entities.videos

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey

@Entity(tableName = "video_playlists")
data class VideoPlaylistData(
        @ColumnInfo(name = "name")
        val name: String
) {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    var id: Long = 0L
}