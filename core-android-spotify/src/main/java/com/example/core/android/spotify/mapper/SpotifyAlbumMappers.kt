package com.example.core.android.spotify.mapper

import com.example.spotifyapi.models.Album
import com.example.spotifyapi.util.secondIconUrlOrDefault
import com.example.there.domain.entity.spotify.AlbumEntity
import com.example.there.domain.entity.spotify.SimpleArtistEntity

val Album.domain: AlbumEntity
    get() = AlbumEntity(
        id = id,
        name = name,
        albumType = albumType.name,
        artists = artists.map { SimpleArtistEntity(it.id, it.name) },
        iconUrl = images.secondIconUrlOrDefault,
        uri = uri
    )
