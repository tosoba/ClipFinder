package com.example.core.android.spotify.mapper

import com.example.spotifyapi.models.Playlist
import com.example.spotifyapi.util.firstIconUrlOrDefault
import com.example.there.domain.entity.spotify.PlaylistEntity

val Playlist.domain: PlaylistEntity
    get() = PlaylistEntity(
        id = id,
        name = name,
        iconUrl = images.firstIconUrlOrDefault,
        userId = owner?.id,
        uri = uri
    )
