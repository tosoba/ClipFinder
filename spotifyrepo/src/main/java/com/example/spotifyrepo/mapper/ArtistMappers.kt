package com.example.spotifyrepo.mapper

import com.example.core.model.StringUrlModel
import com.example.db.model.spotify.ArtistDbModel
import com.example.spotifyapi.model.ArtistApiModel
import com.example.spotifyrepo.util.secondIconUrlOrDefault
import com.example.there.domain.entity.spotify.ArtistEntity


val ArtistDbModel.domain: ArtistEntity
    get() = ArtistEntity(
            id = id,
            name = name,
            iconUrl = icons.secondIconUrlOrDefault,
            popularity = popularity
    )

val ArtistEntity.db: ArtistDbModel
    get() = ArtistDbModel(
            id = id,
            name = name,
            popularity = popularity,
            icons = listOf(StringUrlModel(url = iconUrl))
    )

val ArtistApiModel.domain: ArtistEntity
    get() = ArtistEntity(
            id = id,
            name = name,
            iconUrl = icons.secondIconUrlOrDefault,
            popularity = popularity
    )