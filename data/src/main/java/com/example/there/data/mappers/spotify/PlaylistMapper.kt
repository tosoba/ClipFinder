package com.example.there.data.mappers.spotify

import com.example.there.data.entities.spotify.IconData
import com.example.there.data.entities.spotify.OwnerData
import com.example.there.data.entities.spotify.PlaylistData
import com.example.there.data.util.firstIconUrlOrDefault
import com.example.there.domain.common.OneWayMapper
import com.example.there.domain.common.TwoWayMapper
import com.example.there.domain.entities.spotify.PlaylistEntity

object PlaylistMapper : TwoWayMapper<PlaylistData, PlaylistEntity>() {

    override fun mapFrom(from: PlaylistData): PlaylistEntity = PlaylistEntity(
            id = from.id,
            name = from.name,
            iconUrl = from.icons.firstIconUrlOrDefault,
            userId = from.owner.id
    )

    override fun mapBack(from: PlaylistEntity): PlaylistData = PlaylistData(
            id = from.id,
            name = from.name,
            owner = OwnerData(from.userId),
            icons = listOf(IconData(url = from.iconUrl))
    )
}