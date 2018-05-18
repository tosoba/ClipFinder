package com.example.there.data.mappers.videos

import com.example.there.data.entities.videos.VideoPlaylistData
import com.example.there.domain.common.Mapper
import com.example.there.domain.entities.videos.VideoPlaylistEntity

object VideoPlaylistMapper : Mapper<VideoPlaylistData, VideoPlaylistEntity>() {
    override fun mapFrom(from: VideoPlaylistData): VideoPlaylistEntity = VideoPlaylistEntity(
            id = from.id,
            name = from.name
    )
}