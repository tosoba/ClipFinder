package com.example.there.data.mapper.spotify

import com.example.there.data.entities.spotify.PlaylistData
import com.example.there.data.util.firstIconUrlOrDefault
import com.example.there.domain.common.Mapper
import com.example.there.domain.entities.spotify.PlaylistEntity

object PlaylistMapper : Mapper<PlaylistData, PlaylistEntity>() {
    override fun mapFrom(from: PlaylistData): PlaylistEntity = PlaylistEntity(
            id = from.id,
            name = from.name,
            iconUrl = from.icons.firstIconUrlOrDefault
    )
}