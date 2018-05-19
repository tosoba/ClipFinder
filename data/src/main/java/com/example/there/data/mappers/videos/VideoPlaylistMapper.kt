package com.example.there.data.mappers.videos

import com.example.there.data.entities.videos.VideoPlaylistData
import com.example.there.domain.common.OneWayMapper
import com.example.there.domain.common.TwoWayMapper
import com.example.there.domain.entities.videos.VideoPlaylistEntity

object VideoPlaylistMapper : TwoWayMapper<VideoPlaylistData, VideoPlaylistEntity>() {
    override fun mapFrom(from: VideoPlaylistData): VideoPlaylistEntity = VideoPlaylistEntity(
            id = from.id,
            name = from.name
    )

    override fun mapBack(from: VideoPlaylistEntity): VideoPlaylistData = VideoPlaylistData(name = from.name)
}