package com.example.youtubeapi.model

class ThumbnailsApiModel(
        val default: ThumbnailApiModel?,
        val medium: ThumbnailApiModel?,
        val high: ThumbnailApiModel?,
        val maxres: ThumbnailApiModel?,
        val standard: ThumbnailApiModel?
)

class ThumbnailApiModel(
        val url: String,
        val width: Int,
        val height: Int
)