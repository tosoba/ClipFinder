package com.example.there.findclips.model.mappers

import com.example.there.domain.common.TwoWayMapper
import com.example.there.domain.entities.spotify.SimpleArtistEntity
import com.example.there.domain.entities.spotify.TrackEntity
import com.example.there.findclips.model.entities.SimpleArtist
import com.example.there.findclips.model.entities.Track

object TrackEntityMapper : TwoWayMapper<TrackEntity, Track>() {
    override fun mapFrom(from: TrackEntity): Track = Track(
            id = from.id,
            name = from.name,
            iconUrl = from.iconUrl,
            albumId = from.albumId,
            albumName = from.albumName,
            artists = from.artists.map { SimpleArtist(it.id, it.name) },
            popularity = from.popularity,
            trackNumber = from.trackNumber
    )

    override fun mapBack(from: Track): TrackEntity = TrackEntity(
            id = from.id,
            name = from.name,
            iconUrl = from.iconUrl,
            albumId = from.albumId,
            albumName = from.albumName,
            artists = from.artists.map { SimpleArtistEntity(it.id, it.name) },
            popularity = from.popularity,
            trackNumber = from.trackNumber
    )
}