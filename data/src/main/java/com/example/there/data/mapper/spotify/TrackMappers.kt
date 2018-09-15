package com.example.there.data.mapper.spotify

import com.example.there.data.entity.spotify.IconData
import com.example.there.data.entity.spotify.SimplifiedAlbumData
import com.example.there.data.entity.spotify.SimplifiedArtistData
import com.example.there.data.entity.spotify.TrackData
import com.example.there.data.util.secondIconUrlOrDefault
import com.example.there.domain.common.OneWayMapper
import com.example.there.domain.common.TwoWayMapper
import com.example.there.domain.entity.spotify.SimpleArtistEntity
import com.example.there.domain.entity.spotify.TrackEntity

object TrackMapper : TwoWayMapper<TrackData, TrackEntity>() {
    override fun mapFrom(from: TrackData): TrackEntity = TrackEntity(
            id = from.id,
            name = from.name,
            iconUrl = from.album.icons.secondIconUrlOrDefault,
            albumId = from.album.id,
            albumName = from.album.name,
            artists = from.artists.map { SimpleArtistEntity(it.id, it.name) },
            popularity = from.popularity,
            trackNumber = from.trackNumber,
            uri = from.uri,
            durationMs = from.durationMs
    )

    override fun mapBack(from: TrackEntity): TrackData = TrackData(
            id = from.id,
            name = from.name,
            album = SimplifiedAlbumData(
                    id = from.albumId,
                    name = from.albumName,
                    icons = listOf(IconData(url = from.iconUrl))
            ),
            artists = from.artists.map { SimplifiedArtistData(it.id, it.name) },
            popularity = from.popularity,
            trackNumber = from.trackNumber,
            uri = from.uri,
            durationMs = from.durationMs
    )
}

object ChartTrackIdMapper : OneWayMapper<String, String>() {
    override fun mapFrom(from: String): String = from.substring(from.lastIndexOf('/') + 1)
}