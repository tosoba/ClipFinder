package com.example.there.data.mapper.spotify

import com.example.there.data.entity.spotify.IconData
import com.example.there.data.entity.spotify.SimplifiedAlbumData
import com.example.there.data.entity.spotify.SimplifiedArtistData
import com.example.there.data.entity.spotify.TrackData
import com.example.there.data.util.secondIconUrlOrDefault
import com.example.there.domain.entity.spotify.SimpleArtistEntity
import com.example.there.domain.entity.spotify.TrackEntity


val TrackData.domain: TrackEntity
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

val TrackEntity.data: TrackData
    get() = TrackData(
            id = id,
            name = name,
            album = SimplifiedAlbumData(
                    id = albumId,
                    name = albumName,
                    icons = listOf(IconData(url = iconUrl))
            ),
            artists = artists.map { SimplifiedArtistData(it.id, it.name) },
            popularity = popularity,
            trackNumber = trackNumber,
            uri = uri,
            durationMs = durationMs
    )

object ChartTrackIdMapper {
    fun map(from: String): String = from.substring(from.lastIndexOf('/') + 1)
}