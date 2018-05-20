package com.example.there.findclips.model.mappers

import com.example.there.domain.common.TwoWayMapper
import com.example.there.domain.entities.videos.VideoPlaylistEntity
import com.example.there.findclips.model.entities.VideoPlaylist

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