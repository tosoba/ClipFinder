package com.example.there.findclips.model.mapper.spotify

import com.example.there.domain.entity.spotify.PlaylistEntity
import com.example.there.findclips.model.entity.spotify.Playlist

val PlaylistEntity.ui: Playlist
    get() = Playlist(
            id = id,
            name = name,
            iconUrl = iconUrl,
            userId = userId,
            uri = uri
    )

val Playlist.domain: PlaylistEntity
    get() = PlaylistEntity(
            id = id,
            name = name,
            iconUrl = iconUrl,
            userId = userId,
            uri = uri
    )