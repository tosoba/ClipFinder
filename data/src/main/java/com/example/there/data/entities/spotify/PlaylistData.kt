package com.example.there.data.entities.spotify

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import android.arch.persistence.room.TypeConverters
import com.example.there.data.db.IconDataConverter
import com.example.there.data.db.OwnerDataConverter
import com.google.gson.annotations.SerializedName

@Entity(tableName = "spotify_playlists")
data class PlaylistData(
        @PrimaryKey
        @ColumnInfo(name = "id")
        val id: String,

        @ColumnInfo(name = "icons")
        @SerializedName("images")
        @TypeConverters(IconDataConverter::class)
        val icons: List<IconData>,

        @ColumnInfo(name = "name")
        val name: String,

        @ColumnInfo(name = "owner")
        @TypeConverters(OwnerDataConverter::class)
        val owner: OwnerData
)

data class OwnerData(val id: String)