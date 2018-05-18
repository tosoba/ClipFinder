package com.example.there.domain.entities.videos

import java.math.BigInteger

data class VideoEntity(val id: String,
                       val channelId: String,
                       val title: String,
                       val description: String,
                       val publishedAt: String,
                       val thumbnailUrl: String,
                       val duration: DurationEntity = DurationEntity(""),
                       val viewCount: BigInteger = BigInteger.ZERO)