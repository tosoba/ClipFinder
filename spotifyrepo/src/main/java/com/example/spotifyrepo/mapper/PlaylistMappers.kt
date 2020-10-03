package com.example.spotifyrepo.mapper

import com.example.spotifyapi.model.PlaylistApiModel
import com.example.spotifyrepo.util.firstIconUrlOrDefault
import com.example.there.domain.entity.spotify.PlaylistEntity

val PlaylistApiModel.domain: PlaylistEntity
    get() = PlaylistEntity(
        id = id,
        name = name,
        iconUrl = icons.firstIconUrlOrDefault,
        userId = owner?.id,
        uri = uri
    )
