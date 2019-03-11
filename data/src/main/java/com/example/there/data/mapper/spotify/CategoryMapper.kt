package com.example.there.data.mapper.spotify

import com.example.there.data.entity.spotify.CategoryData
import com.example.there.data.entity.spotify.IconData
import com.example.there.data.util.firstIconUrlOrDefault
import com.example.there.domain.entity.spotify.CategoryEntity


val CategoryData.domain: CategoryEntity
    get() = CategoryEntity(
            id = id,
            name = name,
            iconUrl = icons.firstIconUrlOrDefault
    )

val CategoryEntity.data: CategoryData
    get() = CategoryData(
            id = id,
            name = name,
            icons = listOf(IconData(url = iconUrl)) 
    )