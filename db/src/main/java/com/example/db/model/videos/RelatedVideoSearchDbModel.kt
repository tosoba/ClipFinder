package com.example.db.model.videos

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey

@Entity(tableName = "related_video_search_data")
data class RelatedVideoSearchDbModel(
        @PrimaryKey
        @ColumnInfo(name = "related_video_id")
        val relatedVideoId: String,

        @ColumnInfo(name = "next_page_token")
        val nextPageToken: String?
)