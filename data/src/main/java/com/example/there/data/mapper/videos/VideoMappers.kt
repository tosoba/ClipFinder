package com.example.there.data.mapper.videos

import com.example.db.model.videos.VideoDbModel
import com.example.there.data.util.urlHigh
import com.example.there.domain.entity.videos.VideoEntity
import com.example.there.domain.util.convertDuration
import com.example.youtubeapi.model.VideoApiModel
import java.math.BigInteger


val VideoApiModel.domain: VideoEntity
    get() = VideoEntity(
            id = id,
            channelId = snippet.channelId,
            title = snippet.title,
            description = snippet.description,
            publishedAt = snippet.publishedAt,
            thumbnailUrl = snippet.thumbnails.urlHigh,
            duration = convertDuration(contentDetails.duration),
            viewCount = BigInteger.valueOf(statistics.viewCount?.toLong() ?: 0)
    )

val VideoDbModel.domain: VideoEntity
    get() = VideoEntity(
            id = id,
            channelId = channelId,
            title = title,
            description = description,
            publishedAt = publishedAt,
            thumbnailUrl = thumbnailUrl,
            duration = duration,
            viewCount = BigInteger.valueOf(viewCount),
            channelThumbnailUrl = channelThumbnailUrl,
            playlistId = playlistId,
            query = query,
            relatedVideoId = relatedVideoId
    )

val VideoEntity.db: VideoDbModel
    get() = VideoDbModel(
            id = id,
            channelId = channelId,
            title = title,
            description = description,
            publishedAt = publishedAt,
            thumbnailUrl = thumbnailUrl,
            duration = duration,
            viewCount = viewCount.toLong(),
            channelThumbnailUrl = channelThumbnailUrl ?: "",
            playlistId = playlistId,
            query = query,
            relatedVideoId = relatedVideoId
    )
