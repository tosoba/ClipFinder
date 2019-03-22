package com.example.spotifyrepo.mapper

import com.example.core.model.StringIdModel
import com.example.core.model.StringUrlModel
import com.example.db.model.spotify.PlaylistDbModel
import com.example.spotifyapi.model.PlaylistApiModel
import com.example.spotifyrepo.util.firstIconUrlOrDefault
import com.example.there.domain.entity.spotify.PlaylistEntity

val PlaylistDbModel.domain: PlaylistEntity
    get() = PlaylistEntity(
            id = id,
            name = name,
            iconUrl = icons.map { StringUrlModel(it) }.firstIconUrlOrDefault,
            userId = owner,
            uri = uri
    )

val PlaylistEntity.db: PlaylistDbModel
    get() = PlaylistDbModel(
            id = id,
            name = name,
            owner = userId,
            icons = listOf(iconUrl),
            uri = uri
    )

val PlaylistApiModel.domain: PlaylistEntity
    get() = PlaylistEntity(
            id = id,
            name = name,
            iconUrl = icons.firstIconUrlOrDefault,
            userId = owner.id,
            uri = uri
    )