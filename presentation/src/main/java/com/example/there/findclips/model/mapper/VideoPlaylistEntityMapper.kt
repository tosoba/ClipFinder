package com.example.there.findclips.model.mapper

import com.example.there.domain.common.TwoWayMapper
import com.example.there.domain.entity.videos.VideoPlaylistEntity
import com.example.there.findclips.model.entity.videos.VideoPlaylist

object VideoPlaylistEntityMapper : TwoWayMapper<VideoPlaylistEntity, VideoPlaylist>() {
    override fun mapFrom(from: VideoPlaylistEntity): VideoPlaylist = VideoPlaylist(
            id = from.id,
            name = from.name
    )

    override fun mapBack(from: VideoPlaylist): VideoPlaylistEntity = VideoPlaylistEntity(
            id = from.id,
            name = from.name
    )
}