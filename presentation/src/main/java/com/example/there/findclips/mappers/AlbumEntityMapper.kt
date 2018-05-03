package com.example.there.findclips.mappers

import com.example.there.domain.common.Mapper
import com.example.there.domain.entities.spotify.AlbumEntity
import com.example.there.findclips.entities.Album
import com.example.there.findclips.entities.SimpleArtist

object AlbumEntityMapper : Mapper<AlbumEntity, Album>() {
    override fun mapFrom(from: AlbumEntity): Album = Album(
            id = from.id,
            name = from.name,
            artists = from.artists.map { SimpleArtist(it.id, it.name) },
            albumType = from.albumType,
            iconUrl = from.iconUrl
    )
}