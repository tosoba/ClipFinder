package com.example.there.data.mapper.spotify

import com.example.there.data.entities.spotify.CategoryData
import com.example.there.data.util.firstIconUrlOrDefault
import com.example.there.domain.common.Mapper
import com.example.there.domain.entities.spotify.CategoryEntity
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CategoryMapper @Inject constructor(): Mapper<CategoryData, CategoryEntity>() {
    override fun mapFrom(from: CategoryData): CategoryEntity = CategoryEntity(
            id = from.id,
            name = from.name,
            iconUrl = from.icons.firstIconUrlOrDefault
    )
}