package com.example.core.android.spotify.mapper

import com.example.spotifyapi.models.Artist
import com.example.spotifyapi.util.secondIconUrlOrDefault
import com.example.there.domain.entity.spotify.ArtistEntity

val Artist.domain: ArtistEntity
    get() = ArtistEntity(
        id = id,
        name = name,
        iconUrl = images.secondIconUrlOrDefault,
        popularity = popularity
    )
