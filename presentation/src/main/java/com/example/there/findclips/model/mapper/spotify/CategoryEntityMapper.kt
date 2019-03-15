package com.example.there.findclips.model.mapper.spotify

import com.example.there.domain.entity.spotify.CategoryEntity
import com.example.there.findclips.model.entity.spotify.Category

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