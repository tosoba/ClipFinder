package com.example.youtubeapi.model

class ChannelApiModel(
    val snippet: ChannelSnippet
)

class ChannelSnippet(
    val title: String,
    val description: String,
    val customUrl: String,
    val publishedAt: String,
    val thumbnails: ThumbnailsApiModel
)
