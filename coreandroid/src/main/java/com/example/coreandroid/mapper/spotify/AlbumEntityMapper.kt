package com.example.coreandroid.mapper.spotify

import com.example.coreandroid.model.spotify.Album
import com.example.coreandroid.model.spotify.SimpleArtist
import com.example.there.domain.entity.spotify.AlbumEntity
import com.example.there.domain.entity.spotify.SimpleArtistEntity

val AlbumEntity.ui: Album
    get() = Album(
            id = id,
            name = name,
            artists = artists.map { SimpleArtist(it.id, it.name) },
            albumType = albumType,
            iconUrl = iconUrl,
            uri = uri
    )

val Album.domain: AlbumEntity
    get() = AlbumEntity(
            id = id,
            name = name,
            artists = artists.map { SimpleArtistEntity(it.id, it.name) },
            albumType = albumType,
            iconUrl = iconUrl,
            uri = uri
    )