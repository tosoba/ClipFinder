package com.example.there.data.mapper.videos

import com.example.there.data.entities.videos.VideoData
import com.example.there.data.util.urlHigh
import com.example.there.domain.common.Mapper
import com.example.there.domain.entities.videos.VideoEntity
import com.example.there.domain.util.Duration
import java.math.BigInteger
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class VideoMapper @Inject constructor() : Mapper<VideoData, VideoEntity>() {
    override fun mapFrom(from: VideoData): VideoEntity = VideoEntity(
            id = from.id,
            channelId = from.snippet.channelId,
            title = from.snippet.title,
            description = from.snippet.description,
            publishedAt = from.snippet.publishedAt,
            thumbnailUrl = from.snippet.thumbnails.urlHigh,
            duration = Duration(from.contentDetails.duration),
            viewCount = BigInteger.valueOf(from.statistics.viewCount?.toLong() ?: 0)
    )
}