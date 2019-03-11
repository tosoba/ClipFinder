package com.example.there.data.mapper.spotify

import com.example.there.data.entity.spotify.IconData
import com.example.there.data.entity.spotify.OwnerData
import com.example.there.data.entity.spotify.PlaylistData
import com.example.there.data.util.firstIconUrlOrDefault
import com.example.there.domain.entity.spotify.PlaylistEntity

val PlaylistData.domain: PlaylistEntity
    get() = PlaylistEntity(
            id = id,
            name = name,
            iconUrl = icons.firstIconUrlOrDefault,
            userId = owner.id,
            uri = uri
    )

val PlaylistEntity.data: PlaylistData
    get() = PlaylistData(
            id = id,
            name = name,
            owner = OwnerData(userId),
            icons = listOf(IconData(url = iconUrl)),
            uri = uri
    )