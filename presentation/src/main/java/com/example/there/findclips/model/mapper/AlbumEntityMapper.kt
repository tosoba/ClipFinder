package com.example.there.findclips.model.mapper

import com.example.there.domain.entity.spotify.AlbumEntity
import com.example.there.domain.entity.spotify.SimpleArtistEntity
import com.example.there.findclips.model.entity.spotify.Album
import com.example.there.findclips.model.entity.spotify.SimpleArtist

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