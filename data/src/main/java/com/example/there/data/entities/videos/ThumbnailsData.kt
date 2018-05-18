package com.example.there.data.entities.videos

data class ThumbnailsData(
        val default: ThumbnailData?,
        val medium: ThumbnailData?,
        val high: ThumbnailData?,
        val maxres: ThumbnailData?,
        val standard: ThumbnailData?
)

data class ThumbnailData(
        val url: String,
        val width: Int,
        val height: Int
)