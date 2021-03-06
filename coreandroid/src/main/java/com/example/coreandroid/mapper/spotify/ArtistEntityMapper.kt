package com.example.coreandroid.mapper.spotify

import com.example.coreandroid.model.spotify.Artist
import com.example.there.domain.entity.spotify.ArtistEntity

val ArtistEntity.ui: Artist
    get() = Artist(
            id = id,
            name = name,
            popularity = popularity,
            iconUrl = iconUrl
    )

val Artist.domain: ArtistEntity
    get() = ArtistEntity(
            id = id,
            name = name,
            popularity = popularity,
            iconUrl = iconUrl
    )