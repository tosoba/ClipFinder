package com.example.core.android.mapper.spotify

import com.example.core.android.model.spotify.Playlist
import com.example.there.domain.entity.spotify.PlaylistEntity

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
