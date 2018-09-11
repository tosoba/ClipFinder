package com.example.there.data.mapper.spotify

import com.example.there.data.entity.spotify.ArtistData
import com.example.there.data.entity.spotify.IconData
import com.example.there.data.util.secondIconUrlOrDefault
import com.example.there.domain.common.TwoWayMapper
import com.example.there.domain.entity.spotify.ArtistEntity

object ArtistMapper : TwoWayMapper<ArtistData, ArtistEntity>() {

    override fun mapFrom(from: ArtistData): ArtistEntity = ArtistEntity(
            id = from.id,
            name = from.name,
            iconUrl = from.icons.secondIconUrlOrDefault,
            popularity = from.popularity
    )

    override fun mapBack(from: ArtistEntity): ArtistData = ArtistData(
            id = from.id,
            name = from.name,
            popularity = from.popularity,
            icons = listOf(IconData(url = from.iconUrl))
    )
}