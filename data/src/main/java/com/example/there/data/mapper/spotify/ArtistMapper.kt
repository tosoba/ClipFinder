package com.example.there.data.mapper.spotify

import com.example.there.data.entities.spotify.ArtistData
import com.example.there.data.util.secondIconUrlOrDefault
import com.example.there.domain.common.Mapper
import com.example.there.domain.entities.spotify.ArtistEntity

object ArtistMapper : Mapper<ArtistData, ArtistEntity>() {
    override fun mapFrom(from: ArtistData): ArtistEntity = ArtistEntity(
            id = from.id,
            name = from.name,
            iconUrl = from.icons.secondIconUrlOrDefault,
            popularity = from.popularity
    )
}