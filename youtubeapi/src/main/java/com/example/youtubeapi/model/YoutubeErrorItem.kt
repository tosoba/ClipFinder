package com.example.youtubeapi.model

data class YoutubeErrorItem(
    val reason: String,
    val domain: String,
    val locationType: String,
    val location: String,
    val message: String
)
