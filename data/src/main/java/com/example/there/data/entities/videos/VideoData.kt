package com.example.there.data.entities.videos

import com.google.gson.annotations.SerializedName

data class VideoData(
        val id: String,
        val snippet: VideoSnippet,
        val contentDetails: VideoContentDetails,
        val statistics: Statistics
)

data class VideoSnippet(
        val publishedAt: String,
        val channelId: String,
        val title: String,
        val description: String,
        val thumbnails: Thumbnails,
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