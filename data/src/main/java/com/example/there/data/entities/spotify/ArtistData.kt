package com.example.there.data.entities.spotify

import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import android.arch.persistence.room.TypeConverters
import com.example.there.data.db.IconDataConverter
import com.google.gson.annotations.SerializedName

@Entity(tableName = "artists")
data class ArtistData(
        @PrimaryKey
        val id: String,

        @SerializedName("images")
        @TypeConverters(IconDataConverter::class)
        val icons: List<IconData>,

        val name: String,

        val popularity: Int
)

data class SimplifiedArtistData(
        val id: String,
        val name: String
)