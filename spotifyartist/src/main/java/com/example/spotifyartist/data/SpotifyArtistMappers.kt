package com.example.spotifyartist.data

import com.example.db.model.spotify.ArtistDbModel
import com.example.spotifyapi.models.Artist
import com.example.spotifyapi.util.secondIconUrlOrDefault
import com.example.there.domain.entity.spotify.ArtistEntity

val ArtistEntity.db: ArtistDbModel
    get() = ArtistDbModel(
        id = id,
        name = name,
        popularity = popularity,
        icons = listOf(iconUrl)
    )

val Artist.domain: ArtistEntity
    get() = ArtistEntity(
        id = id,
        name = name,
        iconUrl = images.secondIconUrlOrDefault,
        popularity = popularity
    )
