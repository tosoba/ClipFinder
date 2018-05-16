package com.example.there.findclips.model.mappers

import com.example.there.domain.common.Mapper
import com.example.there.domain.entities.spotify.PlaylistEntity
import com.example.there.findclips.model.entities.Playlist

object PlaylistEntityMapper : Mapper<PlaylistEntity, Playlist>() {
    override fun mapFrom(from: PlaylistEntity): Playlist = Playlist(
            id = from.id,
            name = from.name,
            iconUrl = from.iconUrl,
            userId = from.userId
    )
}