package com.example.db.model.videos

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "video_search_data")
data class VideoSearchDbModel(
        @PrimaryKey
        @ColumnInfo(name = "search_query")
        val query: String,

        @ColumnInfo(name = "next_page_token")
        val nextPageToken: String?
)