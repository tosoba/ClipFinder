package com.example.there.findclips.model.mapper

import com.example.there.domain.entity.spotify.ArtistEntity
import com.example.there.findclips.model.entity.spotify.Artist

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