package com.example.there.data.entities.videos

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import com.google.gson.annotations.SerializedName


data class VideoData(val id: String,
                     val snippet: VideoSnippet,
                     val contentDetails: VideoContentDetails,
                     val statistics: Statistics)

@Entity(tableName = "videos")
data class VideoDbData(
        @PrimaryKey
        @ColumnInfo(name = "id")
        val id: String,

        @ColumnInfo(name = "channel_id")
        val channelId: String,

        @ColumnInfo(name = "title")
        val title: String,

        @ColumnInfo(name = "description")
        val description: String,

        @ColumnInfo(name = "published_at")
        val publishedAt: String,

        @ColumnInfo(name = "thumbnail_url")
        val thumbnailUrl: String,

        @ColumnInfo(name = "duration")
        val duration: String,

        @ColumnInfo(name = "view_count")
        val viewCount: Long,

        @ColumnInfo(name = "playlist_id")
        var playlistId: Long? = null,

        @ColumnInfo(name = "channel_thumbnail_url")
        val channelThumbnailUrl: String
)

data class VideoSnippet(
        val publishedAt: String,
        val channelId: String,
        val title: String,
        val description: String,
        val thumbnails: ThumbnailsData,
        val channelTitle: String,
        val tags: List<String>
)

data class VideoContentDetails(
        val duration: String
)

data class Statistics(
        val viewCount: String?,
        val likeCount: String?,
        val dislikeCount: String?,
        val favoriteCount: String?,
        val commentCount: String?
)

data class VideoSearchData(
        val id: VideoSearchId
)

data class VideoSearchId(
        @SerializedName("videoId") val id: String
)