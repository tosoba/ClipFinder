package com.example.there.data.mappers.spotify

import com.example.there.data.entities.spotify.TrackData
import com.example.there.data.util.secondIconUrlOrDefault
import com.example.there.domain.common.Mapper
import com.example.there.domain.entities.spotify.SimpleArtistEntity
import com.example.there.domain.entities.spotify.TrackEntity

object TrackMapper : Mapper<TrackData, TrackEntity>() {
    override fun mapFrom(from: TrackData): TrackEntity = TrackEntity(
            id = from.id,
            name = from.name,
            iconUrl = from.album.icons.secondIconUrlOrDefault,
            albumId = from.album.id,
            albumName = from.album.name,
            artists = from.artists.map { SimpleArtistEntity(it.id, it.name) },
            popularity = from.popularity,
            trackNumber = from.trackNumber
    )
}

object ChartTrackIdMapper : Mapper<String, String>() {
    override fun mapFrom(from: String): String = from.substring(from.lastIndexOf('/') + 1)
}