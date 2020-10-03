package com.example.spotifyrepo.mapper

import com.example.spotifyapi.model.TrackApiModel
import com.example.spotifyrepo.util.secondIconUrlOrDefault
import com.example.there.domain.entity.spotify.SimpleArtistEntity
import com.example.there.domain.entity.spotify.TrackEntity

val TrackApiModel.domain: TrackEntity
    get() = TrackEntity(
        id = id,
        name = name,
        iconUrl = album.icons.secondIconUrlOrDefault,
        albumId = album.id,
        albumName = album.name,
        artists = artists.map { SimpleArtistEntity(it.id, it.name) },
        popularity = popularity,
        trackNumber = trackNumber,
        uri = uri,
        durationMs = durationMs
    )
