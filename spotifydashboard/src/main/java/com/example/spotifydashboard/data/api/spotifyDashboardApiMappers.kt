package com.example.spotifydashboard.data.api

import com.example.spotifydashboard.data.api.model.CategoryApiModel
import com.example.spotifyrepo.util.firstIconUrlOrDefault
import com.example.there.domain.entity.spotify.CategoryEntity

val CategoryApiModel.domain: CategoryEntity
    get() = CategoryEntity(
            id = id,
            name = name,
            iconUrl = icons.firstIconUrlOrDefault
    )

object ChartTrackIdMapper {
    fun map(from: String): String = from.substring(from.lastIndexOf('/') + 1)
}