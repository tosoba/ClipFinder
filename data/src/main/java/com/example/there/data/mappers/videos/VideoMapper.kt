package com.example.there.data.mappers.videos

import com.example.there.data.entities.videos.VideoData
import com.example.there.data.util.urlHigh
import com.example.there.domain.common.Mapper
import com.example.there.domain.entities.videos.VideoEntity
import com.example.there.domain.entities.videos.DurationEntity
import java.math.BigInteger

object VideoMapper : Mapper<VideoData, VideoEntity>() {
    override fun mapFrom(from: VideoData): VideoEntity = VideoEntity(
            id = from.id,
            channelId = from.snippet.channelId,
            title = from.snippet.title,
            description = from.snippet.description,
            publishedAt = from.snippet.publishedAt,
            thumbnailUrl = from.snippet.thumbnails.urlHigh,
            duration = DurationEntity(from.contentDetails.duration),
            viewCount = BigInteger.valueOf(from.statistics.viewCount?.toLong() ?: 0)
    )
}