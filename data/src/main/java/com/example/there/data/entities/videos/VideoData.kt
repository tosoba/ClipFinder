package com.example.there.data.entities.videos

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import android.arch.persistence.room.TypeConverters
import com.example.there.data.db.VideoContentDetailsConverter
import com.example.there.data.db.VideoSnippetConverter
import com.example.there.data.db.VideoStatisticsConverter
import com.google.gson.annotations.SerializedName

@Entity(tableName = "videos")
data class VideoData(
        @PrimaryKey
        @ColumnInfo(name = "id")
        val id: String,

        @ColumnInfo(name = "snippet")
        @TypeConverters(VideoSnippetConverter::class)
        val snippet: VideoSnippet,

        @ColumnInfo(name = "content_details")
        @TypeConverters(VideoContentDetailsConverter::class)
        val contentDetails: VideoContentDetails,

        @ColumnInfo(name = "statistics")
        @TypeConverters(VideoStatisticsConverter::class)
        val statistics: Statistics,

        @ColumnInfo(name = "playlistId")
        var playlistId: Long? = null
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