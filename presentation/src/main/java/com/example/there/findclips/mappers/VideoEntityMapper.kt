package com.example.there.findclips.mappers

import android.databinding.ObservableField
import com.example.there.domain.common.Mapper
import com.example.there.domain.entities.videos.VideoEntity
import com.example.there.findclips.entities.Video
import javax.inject.Singleton
import javax.inject.Inject

@Singleton
class VideoEntityMapper @Inject constructor(): Mapper<VideoEntity, Video>() {
    override fun mapFrom(from: VideoEntity): Video = Video(
            id = from.id,
            channelId = from.channelId,
            title = from.title,
            description = from.description,
            publishedAt = from.publishedAt,
            thumbnailUrl = from.thumbnailUrl,
            duration = from.duration,
            viewCount = from.viewCount,
            channelThumbnailUrl = ObservableField(from.channelThumbnailUrl ?: "")
    )
}