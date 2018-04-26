package com.example.there.data.mapper

import com.example.there.data.entities.CategoryData
import com.example.there.data.util.firstIconUrlOrDefault
import com.example.there.domain.common.Mapper
import com.example.there.domain.entities.CategoryEntity
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