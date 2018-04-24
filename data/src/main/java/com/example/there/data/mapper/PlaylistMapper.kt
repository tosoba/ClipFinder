package com.example.there.data.mapper

import com.example.there.data.entities.PlaylistData
import com.example.there.data.util.iconUrlOrDefault
import com.example.there.domain.common.Mapper
import com.example.there.domain.entities.PlaylistEntity
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PlaylistMapper @Inject constructor() : Mapper<PlaylistData, PlaylistEntity>() {
    override fun mapFrom(from: PlaylistData): PlaylistEntity = PlaylistEntity(
            id = from.id,
            name = from.name,
            iconUrl = from.icons.iconUrlOrDefault
    )
}