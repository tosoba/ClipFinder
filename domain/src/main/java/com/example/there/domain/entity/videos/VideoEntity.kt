package com.example.there.domain.entity.videos

import java.math.BigInteger

data class VideoEntity(
    val id: String,
    val channelId: String,
    val title: String,
    val description: String,
    val publishedAt: String,
    val thumbnailUrl: String,
    val duration: String,
    val viewCount: BigInteger = BigInteger.ZERO,
    val channelThumbnailUrl: String? = null,
    var playlistId: Long? = null,
    var query: String? = null,
    var relatedVideoId: String? = null
)
