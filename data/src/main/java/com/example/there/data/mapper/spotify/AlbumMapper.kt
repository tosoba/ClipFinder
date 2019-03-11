package com.example.there.data.mapper.spotify

import com.example.there.data.entity.spotify.AlbumData
import com.example.there.data.entity.spotify.IconData
import com.example.there.data.entity.spotify.SimplifiedArtistData
import com.example.there.data.util.secondIconUrlOrDefault
import com.example.there.domain.entity.spotify.AlbumEntity
import com.example.there.domain.entity.spotify.SimpleArtistEntity


val AlbumData.domain: AlbumEntity
    get() = AlbumEntity(
            id = id,
            name = name,
            albumType = albumType,
            artists = artists.map { SimpleArtistEntity(it.id, it.name) },
            iconUrl = icons.secondIconUrlOrDefault,
            uri = uri
    )

val AlbumEntity.data: AlbumData
    get() = AlbumData(
            id = id,
            name = name,
            albumType = albumType,
            artists = artists.map { SimplifiedArtistData(it.id, it.name) },
            icons = listOf(IconData(url = iconUrl)),
            uri = uri
    )