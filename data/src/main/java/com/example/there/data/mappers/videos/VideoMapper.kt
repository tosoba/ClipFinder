package com.example.there.data.mappers.videos

import com.example.there.data.entities.videos.VideoData
import com.example.there.data.entities.videos.VideoDbData
import com.example.there.data.util.urlHigh
import com.example.there.domain.common.OneWayMapper
import com.example.there.domain.common.TwoWayMapper
import com.example.there.domain.common.convertDuration
import com.example.there.domain.entities.videos.VideoEntity
import java.math.BigInteger


object VideoMapper : OneWayMapper<VideoData, VideoEntity>() {
    override fun mapFrom(from: VideoData): VideoEntity = VideoEntity(
            id = from.id,
            channelId = from.snippet.channelId,
            title = from.snippet.title,
            description = from.snippet.description,
            publishedAt = from.snippet.publishedAt,
            thumbnailUrl = from.snippet.thumbnails.urlHigh,
            duration = convertDuration(from.contentDetails.duration),
            viewCount = BigInteger.valueOf(from.statistics.viewCount?.toLong() ?: 0)
    )
}

object VideoDbMapper : TwoWayMapper<VideoDbData, VideoEntity>() {
    override fun mapFrom(from: VideoDbData): VideoEntity = VideoEntity(
            id = from.id,
            channelId = from.channelId,
            title = from.title,
            description = from.description,
            publishedAt = from.publishedAt,
            thumbnailUrl = from.thumbnailUrl,
            duration = from.duration,
            viewCount = BigInteger.valueOf(from.viewCount),
            channelThumbnailUrl = from.channelThumbnailUrl,
            playlistId = from.playlistId,
            query = from.query,
            relatedVideoId = from.relatedVideoId
    )

    override fun mapBack(from: VideoEntity): VideoDbData = VideoDbData(
            id = from.id,
            channelId = from.channelId,
            title = from.title,
            description = from.description,
            publishedAt = from.publishedAt,
            thumbnailUrl = from.thumbnailUrl,
            duration = from.duration,
            viewCount = from.viewCount.toLong(),
            channelThumbnailUrl = from.channelThumbnailUrl ?: "",
            playlistId = from.playlistId,
            query = from.query,
            relatedVideoId = from.relatedVideoId
    )
}