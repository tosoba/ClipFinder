package com.example.db.model.spotify

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.example.db.SimplifiedArtistConverter

@Entity(tableName = "tracks")
data class TrackDbModel(
    @PrimaryKey
    val id: String,

    val name: String,

    @TypeConverters(SimplifiedArtistConverter::class)
    val artists: List<SimplifiedArtistDbModel>,

    @TypeConverters(SimplifiedAlbumDbModel::class)
    val album: SimplifiedAlbumDbModel,

    val popularity: Int,

    @ColumnInfo(name = "track_number")
    val trackNumber: Int,

    val uri: String,

    val durationMs: Int
)
