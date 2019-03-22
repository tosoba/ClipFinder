package com.example.db.model.spotify

import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import android.arch.persistence.room.TypeConverters
import com.example.db.StringListConverter

@Entity(tableName = "artists")
data class ArtistDbModel(
        @PrimaryKey
        val id: String,

        @TypeConverters(StringListConverter::class)
        val icons: List<String>,

        val name: String,

        val popularity: Int
)

data class SimplifiedArtistDbModel(
        val id: String,
        val name: String
)