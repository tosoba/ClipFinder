package com.example.db.model.spotify

import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import android.arch.persistence.room.TypeConverters
import com.example.core.model.StringIdModel
import com.example.core.model.StringUrlModel
import com.example.db.StringIdModelConverter
import com.example.db.StringUrlModelConverter


@Entity(tableName = "spotify_playlists")
data class PlaylistDbModel(
        @PrimaryKey
        val id: String,

        @TypeConverters(StringUrlModelConverter::class)
        val icons: List<StringUrlModel>,

        val name: String,

        @TypeConverters(StringIdModelConverter::class)
        val owner: StringIdModel,

        val uri: String
)
