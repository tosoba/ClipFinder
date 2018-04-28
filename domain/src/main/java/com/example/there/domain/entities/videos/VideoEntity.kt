package com.example.there.domain.entities.videos

import com.example.there.domain.util.Duration
import java.math.BigInteger

data class VideoEntity(val id: String,
                       val channelId: String,
                       val title: String,
                       val description: String,
                       val publishedAt: String,
                       val thumbnailUrl: String,
                       val duration: Duration = Duration(""),
                       val viewCount: BigInteger = BigInteger.ZERO)