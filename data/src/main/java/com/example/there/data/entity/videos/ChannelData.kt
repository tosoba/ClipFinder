package com.example.there.data.entity.videos

data class ChannelData(
        val snippet: ChannelSnippet
)

data class ChannelSnippet(
        val title: String,
        val description: String,
        val customUrl: String,
        val publishedAt: String,
        val thumbnails: ThumbnailsData
)