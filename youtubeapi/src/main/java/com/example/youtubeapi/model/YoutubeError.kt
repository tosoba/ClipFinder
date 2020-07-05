package com.example.youtubeapi.model

data class YoutubeError(val code: Int, val message: String, val errors: List<YoutubeErrorItem>?)
