package com.example.there.data.entities.videos

data class Thumbnails(
        val default: Thumbnail?,
        val medium: Thumbnail?,
        val high: Thumbnail?,
        val maxres: Thumbnail?,
        val standard: Thumbnail?
)

data class Thumbnail(
        val url: String,
        val width: Int,
        val height: Int
)