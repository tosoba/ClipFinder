package com.example.coreandroid.mapper.videos

import android.databinding.ObservableField
import com.example.coreandroid.model.videos.Video
import com.example.there.domain.entity.videos.VideoEntity

val VideoEntity.ui: Video
    get() = Video(
            id = id,
            channelId = channelId,
            title = title,
            description = description,
            publishedAt = publishedAt,
            thumbnailUrl = thumbnailUrl,
            duration = duration,
            viewCount = viewCount,
            channelThumbnailUrl = if (channelThumbnailUrl != null) {
                ObservableField(channelThumbnailUrl!!)
            } else {
                ObservableField("")
            },
            playlistId = playlistId,
            relatedVideoId = relatedVideoId,
            query = query
    )

val Video.domain: VideoEntity
    get() = VideoEntity(
            id = id,
            channelId = channelId,
            title = title,
            description = description,
            publishedAt = publishedAt,
            thumbnailUrl = thumbnailUrl,
            duration = duration,
            viewCount = viewCount,
            channelThumbnailUrl = channelThumbnailUrl.get(),
            playlistId = playlistId,
            relatedVideoId = relatedVideoId,
            query = query
    )