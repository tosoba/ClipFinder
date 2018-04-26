package com.example.there.data.mapper.spotify

import com.example.there.data.entities.spotify.TrackData
import com.example.there.data.util.secondIconUrlOrDefault
import com.example.there.domain.common.Mapper
import com.example.there.domain.entities.spotify.TrackArtistEntity
import com.example.there.domain.entities.spotify.TrackEntity
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TrackMapper @Inject constructor() : Mapper<TrackData, TrackEntity>() {
    override fun mapFrom(from: TrackData): TrackEntity = TrackEntity(
            id = from.id,
            name = from.name,
            iconUrl = from.album.icons.secondIconUrlOrDefault,
            albumId = from.album.id,
            albumName = from.album.name,
            artists = from.artists.map { TrackArtistEntity(it.id, it.name) }
    )
}