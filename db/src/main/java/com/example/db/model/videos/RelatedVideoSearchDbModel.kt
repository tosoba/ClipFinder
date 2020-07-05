package com.example.db.model.videos

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "related_video_search_data")
data class RelatedVideoSearchDbModel(
    @PrimaryKey
    @ColumnInfo(name = "related_video_id")
    val relatedVideoId: String,

    @ColumnInfo(name = "next_page_token")
    val nextPageToken: String?
)
