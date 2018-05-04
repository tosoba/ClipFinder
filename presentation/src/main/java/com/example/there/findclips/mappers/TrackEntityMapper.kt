package com.example.there.findclips.mappers

import com.example.there.domain.common.Mapper
import com.example.there.domain.entities.spotify.TrackEntity
import com.example.there.findclips.entities.SimpleArtist
import com.example.there.findclips.entities.Track

object TrackEntityMapper : Mapper<TrackEntity, Track>() {
    override fun mapFrom(from: TrackEntity): Track = Track(
            id = from.id,
            name = from.name,
            iconUrl = from.iconUrl,
            albumId = from.albumId,
            albumName = from.albumName,
            artists = from.artists.map { SimpleArtist(it.id, it.name) }
    )
}