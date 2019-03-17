package com.example.there.data.mapper.spotify

import com.example.core.model.StringUrlModel
import com.example.db.model.spotify.CategoryDbModel
import com.example.spotifyapi.model.CategoryApiModel
import com.example.there.data.util.firstIconUrlOrDefault
import com.example.there.domain.entity.spotify.CategoryEntity


val CategoryDbModel.domain: CategoryEntity
    get() = CategoryEntity(
            id = id,
            name = name,
            iconUrl = icons.firstIconUrlOrDefault
    )

val CategoryEntity.db: CategoryDbModel
    get() = CategoryDbModel(
            id = id,
            name = name,
            icons = listOf(StringUrlModel(url = iconUrl))
    )

val CategoryApiModel.domain: CategoryEntity
    get() = CategoryEntity(
            id = id,
            name = name,
            iconUrl = icons.firstIconUrlOrDefault
    )