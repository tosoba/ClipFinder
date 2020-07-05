package com.example.spotifyrepo.mapper

import com.example.core.model.StringUrlModel
import com.example.db.model.spotify.AlbumDbModel
import com.example.db.model.spotify.SimplifiedArtistDbModel
import com.example.spotifyapi.model.AlbumApiModel
import com.example.spotifyrepo.util.secondIconUrlOrDefault
import com.example.there.domain.entity.spotify.AlbumEntity
import com.example.there.domain.entity.spotify.SimpleArtistEntity

val AlbumDbModel.domain: AlbumEntity
    get() = AlbumEntity(
        id = id,
        name = name,
        albumType = albumType,
        artists = artists.map { SimpleArtistEntity(it.id, it.name) },
        iconUrl = icons.map { StringUrlModel(it) }.secondIconUrlOrDefault,
        uri = uri
    )

val AlbumEntity.db: AlbumDbModel
    get() = AlbumDbModel(
        id = id,
        name = name,
        albumType = albumType,
        artists = artists.map { SimplifiedArtistDbModel(it.id, it.name) },
        icons = listOf(iconUrl),
        uri = uri
    )

val AlbumApiModel.domain: AlbumEntity
    get() = AlbumEntity(
        id = id,
        name = name,
        albumType = albumType,
        artists = artists.map { SimpleArtistEntity(it.id, it.name) },
        iconUrl = icons.secondIconUrlOrDefault,
        uri = uri
    )
