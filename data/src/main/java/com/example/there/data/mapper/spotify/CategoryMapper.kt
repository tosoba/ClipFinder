package com.example.there.data.mapper.spotify

import com.example.there.data.entity.spotify.CategoryData
import com.example.there.data.entity.spotify.IconData
import com.example.there.data.util.firstIconUrlOrDefault
import com.example.there.domain.common.TwoWayMapper
import com.example.there.domain.entity.spotify.CategoryEntity


object CategoryMapper : TwoWayMapper<CategoryData, CategoryEntity>() {

    override fun mapFrom(from: CategoryData): CategoryEntity = CategoryEntity(
            id = from.id,
            name = from.name,
            iconUrl = from.icons.firstIconUrlOrDefault
    )

    override fun mapBack(from: CategoryEntity): CategoryData = CategoryData(
            id = from.id,
            name = from.name,
            icons = listOf(IconData(url = from.iconUrl))
    )
}