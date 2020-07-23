package com.example.spotify.category.data

import com.example.db.model.spotify.CategoryDbModel
import com.example.there.domain.entity.spotify.CategoryEntity

val CategoryEntity.db: CategoryDbModel
    get() = CategoryDbModel(
        id = id,
        name = name,
        icons = listOf(iconUrl)
    )
