package com.example.there.findclips.model.mapper

import com.example.there.domain.common.TwoWayMapper
import com.example.there.domain.entity.spotify.PlaylistEntity
import com.example.there.findclips.model.entity.spotify.Playlist

object PlaylistEntityMapper : TwoWayMapper<PlaylistEntity, Playlist>() {
    override fun mapFrom(from: PlaylistEntity): Playlist = Playlist(
            id = from.id,
            name = from.name,
            iconUrl = from.iconUrl,
            userId = from.userId,
            uri = from.uri
    )

    override fun mapBack(from: Playlist): PlaylistEntity = PlaylistEntity(
            id = from.id,
            name = from.name,
            iconUrl = from.iconUrl,
            userId = from.userId,
            uri = from.uri
    )
}