package com.example.spotifyrepo.mapper

import com.example.spotifyapi.model.ArtistApiModel
import com.example.spotifyrepo.util.secondIconUrlOrDefault
import com.example.there.domain.entity.spotify.ArtistEntity

val ArtistApiModel.domain: ArtistEntity
    get() = ArtistEntity(
        id = id,
        name = name,
        iconUrl = icons.secondIconUrlOrDefault,
        popularity = popularity
    )
