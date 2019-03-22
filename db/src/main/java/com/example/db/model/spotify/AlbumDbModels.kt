package com.example.db.model.spotify

import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import android.arch.persistence.room.TypeConverters
import com.example.db.StringListConverter

@Entity(tableName = "albums")
data class AlbumDbModel(
        val albumType: String,

        val artists: List<SimplifiedArtistDbModel>,

        @PrimaryKey
        val id: String,

        @TypeConverters(StringListConverter::class)
        val icons: List<String>,

        val name: String,

        val uri: String
)

data class SimplifiedAlbumDbModel(
        val id: String,
        val name: String,
        val icons: List<String>
)