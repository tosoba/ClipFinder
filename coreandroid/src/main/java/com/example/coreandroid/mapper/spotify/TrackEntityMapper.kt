package com.example.coreandroid.mapper.spotify

import com.example.coreandroid.model.spotify.SimpleArtist
import com.example.coreandroid.model.spotify.Track
import com.example.there.domain.entity.spotify.SimpleArtistEntity
import com.example.there.domain.entity.spotify.TrackEntity

val TrackEntity.ui: Track
    get() = Track(
            id = id,
            name = name,
            iconUrl = iconUrl,
            albumId = albumId,
            albumName = albumName,
            artists = artists.map { SimpleArtist(it.id, it.name) },
            popularity = popularity,
            trackNumber = trackNumber,
            uri = uri,
            durationMs = durationMs
    )

val Track.domain: TrackEntity
    get() = TrackEntity(
            id = id,
            name = name,
            iconUrl = iconUrl,
            albumId = albumId,
            albumName = albumName,
            artists = artists.map { SimpleArtistEntity(it.id, it.name) },
            popularity = popularity,
            trackNumber = trackNumber,
            uri = uri,
            durationMs = durationMs
    )