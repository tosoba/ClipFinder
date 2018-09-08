package com.example.there.data.mapper.spotify

import com.example.there.data.entity.spotify.IconData
import com.example.there.data.entity.spotify.OwnerData
import com.example.there.data.entity.spotify.PlaylistData
import com.example.there.data.util.firstIconUrlOrDefault
import com.example.there.domain.common.TwoWayMapper
import com.example.there.domain.entity.spotify.PlaylistEntity

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