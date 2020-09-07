package com.example.db.model.spotify

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.example.db.StringListConverter

@Entity(tableName = "spotify_playlists")
data class PlaylistDbModel(
    @PrimaryKey
    val id: String,

    @TypeConverters(StringListConverter::class)
    val icons: List<String>,

    val name: String,

    val owner: String?,

    val uri: String
)
