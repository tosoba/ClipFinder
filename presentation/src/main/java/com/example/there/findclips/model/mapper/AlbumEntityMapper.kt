package com.example.there.findclips.model.mapper

import com.example.there.domain.common.TwoWayMapper
import com.example.there.domain.entity.spotify.AlbumEntity
import com.example.there.domain.entity.spotify.SimpleArtistEntity
import com.example.there.findclips.model.entity.Album
import com.example.there.findclips.model.entity.SimpleArtist

object AlbumEntityMapper : TwoWayMapper<AlbumEntity, Album>() {
    override fun mapFrom(from: AlbumEntity): Album = Album(
            id = from.id,
            name = from.name,
            artists = from.artists.map { SimpleArtist(it.id, it.name) },
            albumType = from.albumType,
            iconUrl = from.iconUrl,
            uri = from.uri
    )

    override fun mapBack(from: Album): AlbumEntity = AlbumEntity(
            id = from.id,
            name = from.name,
            artists = from.artists.map { SimpleArtistEntity(it.id, it.name) },
            albumType = from.albumType,
            iconUrl = from.iconUrl,
            uri = from.uri
    )
}