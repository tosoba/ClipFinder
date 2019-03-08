package com.example.there.findclips.model.mapper

import android.databinding.ObservableField
import com.example.there.domain.common.TwoWayMapper
import com.example.there.domain.entity.videos.VideoEntity
import com.example.there.findclips.model.entity.videos.Video

object VideoEntityMapper : TwoWayMapper<VideoEntity, Video>() {
    override fun mapFrom(from: VideoEntity): Video = Video(
            id = from.id,
            channelId = from.channelId,
            title = from.title,
            description = from.description,
            publishedAt = from.publishedAt,
            thumbnailUrl = from.thumbnailUrl,
            duration = from.duration,
            viewCount = from.viewCount,
            channelThumbnailUrl = if (from.channelThumbnailUrl != null) {
                ObservableField(from.channelThumbnailUrl!!)
            } else {
                ObservableField("")
            },
            playlistId = from.playlistId,
            relatedVideoId = from.relatedVideoId,
            query = from.query
    )

    override fun mapBack(from: Video): VideoEntity = VideoEntity(
            id = from.id,
            channelId = from.channelId,
            title = from.title,
            description = from.description,
            publishedAt = from.publishedAt,
            thumbnailUrl = from.thumbnailUrl,
            duration = from.duration,
            viewCount = from.viewCount,
            channelThumbnailUrl = from.channelThumbnailUrl.get(),
            playlistId = from.playlistId,
            relatedVideoId = from.relatedVideoId,
            query = from.query
    )
}