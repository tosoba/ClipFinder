package com.example.there.findclips.model.mappers

import com.example.there.domain.common.Mapper
import com.example.there.domain.entities.videos.VideoEntity
import com.example.there.findclips.model.entities.Video

object VideoEntityMapper : Mapper<VideoEntity, Video>() {
    override fun mapFrom(from: VideoEntity): Video = Video(
            id = from.id,
            channelId = from.channelId,
            title = from.title,
            description = from.description,
            publishedAt = from.publishedAt,
            thumbnailUrl = from.thumbnailUrl,
            duration = from.duration.toString(),
            viewCount = from.viewCount
    )
}