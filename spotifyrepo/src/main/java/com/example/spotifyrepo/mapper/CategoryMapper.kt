package com.example.spotifyrepo.mapper

import com.example.core.model.StringUrlModel
import com.example.db.model.spotify.CategoryDbModel
import com.example.spotifyrepo.util.firstIconUrlOrDefault
import com.example.there.domain.entity.spotify.CategoryEntity


val CategoryDbModel.domain: CategoryEntity
    get() = CategoryEntity(
            id = id,
            name = name,
            iconUrl = icons.map { StringUrlModel(it) }.firstIconUrlOrDefault
    )

val CategoryEntity.db: CategoryDbModel
    get() = CategoryDbModel(
            id = id,
            name = name,
            icons = listOf(iconUrl)
    )

