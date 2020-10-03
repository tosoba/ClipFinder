package com.example.spotifyrepo.mapper

import com.example.core.model.StringUrlModel
import com.example.spotifyapi.model.AlbumApiModel
import com.example.spotifyrepo.util.secondIconUrlOrDefault
import com.example.there.domain.entity.spotify.AlbumEntity
import com.example.there.domain.entity.spotify.SimpleArtistEntity

val AlbumApiModel.domain: AlbumEntity
    get() = AlbumEntity(
        id = id,
        name = name,
        albumType = albumType,
        artists = artists.map { SimpleArtistEntity(it.id, it.name) },
        iconUrl = icons.secondIconUrlOrDefault,
        uri = uri
    )
