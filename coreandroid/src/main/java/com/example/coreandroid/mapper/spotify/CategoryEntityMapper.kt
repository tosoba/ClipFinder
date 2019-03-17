package com.example.coreandroid.mapper.spotify

import com.example.coreandroid.model.spotify.Category
import com.example.there.domain.entity.spotify.CategoryEntity

val CategoryEntity.ui: Category
    get() = Category(
            id = id,
            name = name,
            iconUrl = iconUrl
    )

val Category.domain: CategoryEntity
    get() = CategoryEntity(
            id = id,
            name = name,
            iconUrl = iconUrl
    )