package com.example.there.findclips.model.mapper.videos

import android.databinding.ObservableField
import com.example.there.domain.entity.videos.VideoEntity
import com.example.there.findclips.model.entity.videos.Video


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