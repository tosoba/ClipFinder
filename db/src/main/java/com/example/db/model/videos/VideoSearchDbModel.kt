package com.example.db.model.videos

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey

@Entity(tableName = "video_search_data")
data class VideoSearchDbModel(
        @PrimaryKey
        @ColumnInfo(name = "search_query")
        val query: String,

        @ColumnInfo(name = "next_page_token")
        val nextPageToken: String?
)