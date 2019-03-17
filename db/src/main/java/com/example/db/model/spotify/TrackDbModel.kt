package com.example.db.model.spotify

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import android.arch.persistence.room.TypeConverters
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