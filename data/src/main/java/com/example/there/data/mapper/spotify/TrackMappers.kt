package com.example.there.data.mapper.spotify

import com.example.core.model.StringUrlModel
import com.example.db.model.spotify.SimplifiedAlbumDbModel
import com.example.db.model.spotify.SimplifiedArtistDbModel
import com.example.db.model.spotify.TrackDbModel
import com.example.spotifyapi.model.TrackApiModel
import com.example.there.data.util.secondIconUrlOrDefault
import com.example.there.domain.entity.spotify.SimpleArtistEntity
import com.example.there.domain.entity.spotify.TrackEntity


val TrackDbModel.domain: TrackEntity
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

val TrackEntity.db: TrackDbModel
    get() = TrackDbModel(
            id = id,
            name = name,
            album = SimplifiedAlbumDbModel(
                    id = albumId,
                    name = albumName,
                    icons = listOf(StringUrlModel(url = iconUrl))
            ),
            artists = artists.map { SimplifiedArtistDbModel(it.id, it.name) },
            popularity = popularity,
            trackNumber = trackNumber,
            uri = uri,
            durationMs = durationMs
    )

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

object ChartTrackIdMapper {
    fun map(from: String): String = from.substring(from.lastIndexOf('/') + 1)
}