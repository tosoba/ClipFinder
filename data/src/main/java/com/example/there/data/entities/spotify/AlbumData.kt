package com.example.there.data.entities.spotify

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import android.arch.persistence.room.TypeConverters
import com.example.there.data.db.IconDataConverter
import com.google.gson.annotations.SerializedName

@Entity(tableName = "albums")
data class AlbumData(
        @SerializedName("album_type") val albumType: String,
        val artists: List<SimplifiedArtistData>,

        @PrimaryKey
        @ColumnInfo(name = "id")
        val id: String,

        @ColumnInfo(name = "icons")
        @SerializedName("images")
        @TypeConverters(IconDataConverter::class)
        val icons: List<IconData>,

        @ColumnInfo(name = "name")
        val name: String
)

data class SimplifiedAlbumData(
        val id: String,
        val name: String,
        @SerializedName("images") val icons: List<IconData>
)