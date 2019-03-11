package com.example.there.data.mapper.spotify

import com.example.there.data.entity.spotify.ArtistData
import com.example.there.data.entity.spotify.IconData
import com.example.there.data.util.secondIconUrlOrDefault
import com.example.there.domain.entity.spotify.ArtistEntity


val ArtistData.domain: ArtistEntity
    get() = ArtistEntity(
            id = id,
            name = name,
            iconUrl = icons.secondIconUrlOrDefault,
            popularity = popularity
    )

val ArtistEntity.data: ArtistData
    get() = ArtistData(
            id = id,
            name = name,
            popularity = popularity,
            icons = listOf(IconData(url = iconUrl))
    )