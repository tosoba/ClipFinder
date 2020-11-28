package com.example.core.android.mapper.videos

import com.example.core.android.model.videos.Video
import com.example.there.domain.entity.videos.VideoEntity

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
