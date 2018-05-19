package com.example.there.findclips.model.mappers

import android.databinding.ObservableField
import com.example.there.domain.common.OneWayMapper
import com.example.there.domain.common.TwoWayMapper
import com.example.there.domain.entities.videos.VideoEntity
import com.example.there.findclips.model.entities.Video

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
            }
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
            channelThumbnailUrl = from.channelThumbnailUrl.get()
    )
}