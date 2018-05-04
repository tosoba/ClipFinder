package com.example.there.data.mappers.spotify

import com.example.there.data.entities.spotify.AlbumData
import com.example.there.data.util.secondIconUrlOrDefault
import com.example.there.domain.common.Mapper
import com.example.there.domain.entities.spotify.AlbumEntity
import com.example.there.domain.entities.spotify.SimpleArtistEntity

object AlbumMapper : Mapper<AlbumData, AlbumEntity>() {
    override fun mapFrom(from: AlbumData): AlbumEntity = AlbumEntity(
            id = from.id,
            name = from.name,
            albumType = from.albumType,
            artists = from.artists.map { SimpleArtistEntity(it.id, it.name) },
            iconUrl = from.icons.secondIconUrlOrDefault
    )
}