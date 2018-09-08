package com.example.there.data.mapper.videos

import com.example.there.data.entity.videos.VideoPlaylistData
import com.example.there.domain.common.TwoWayMapper
import com.example.there.domain.entity.videos.VideoPlaylistEntity

object VideoPlaylistMapper : TwoWayMapper<VideoPlaylistData, VideoPlaylistEntity>() {
    override fun mapFrom(from: VideoPlaylistData): VideoPlaylistEntity = VideoPlaylistEntity(
            id = from.id,
            name = from.name
    )

    override fun mapBack(from: VideoPlaylistEntity): VideoPlaylistData = VideoPlaylistData(name = from.name)
}