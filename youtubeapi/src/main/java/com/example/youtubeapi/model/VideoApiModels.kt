package com.example.youtubeapi.model

import com.google.gson.annotations.SerializedName

class VideoApiModel(
        val id: String,
        val snippet: VideoSnippet,
        val contentDetails: VideoContentDetails,
        val statistics: VideoStatistics
)

class VideoSnippet(
        val publishedAt: String,
        val channelId: String,
        val title: String,
        val description: String,
        val thumbnails: ThumbnailsApiModel,
        val channelTitle: String,
        val tags: List<String>
)

class VideoContentDetails(val duration: String)

class VideoStatistics(
        val viewCount: String?,
        val likeCount: String?,
        val dislikeCount: String?,
        val favoriteCount: String?,
        val commentCount: String?
)

class VideoSearchApiModel(val id: VideoSearchId)

class VideoSearchId(@SerializedName("videoId") val id: String)