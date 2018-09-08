package com.example.there.data.entity.spotify

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import android.arch.persistence.room.TypeConverters
import com.example.there.data.db.SimplifiedAlbumDataConverter
import com.example.there.data.db.SimplifiedArtistDataConverter
import com.google.gson.annotations.SerializedName

@Entity(tableName = "tracks")
data class TrackData(
        @PrimaryKey
        val id: String,

        val name: String,

        @TypeConverters(SimplifiedArtistDataConverter::class)
        val artists: List<SimplifiedArtistData>,

        @TypeConverters(SimplifiedAlbumDataConverter::class)
        val album: SimplifiedAlbumData,

        val popularity: Int,

        @ColumnInfo(name = "track_number")
        @SerializedName("track_number")
        val trackNumber: Int
)

data class SimilarTrackData(
        val id: String,
        val name: String,
        val artists: List<SimplifiedArtistData>
)

data class PlaylistTrackData(
        @SerializedName("added_at")
        val addedAt: String,

        val track: TrackData
)