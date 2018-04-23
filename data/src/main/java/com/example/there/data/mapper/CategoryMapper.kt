package com.example.there.data.mapper

import com.example.there.data.entities.CategoryData
import com.example.there.domain.common.Mapper
import com.example.there.domain.entities.CategoryEntity
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CategoryMapper @Inject constructor(): Mapper<CategoryData, CategoryEntity>() {
    override fun mapFrom(from: CategoryData): CategoryEntity = CategoryEntity(
            id = from.id,
            name = from.name,
            iconUrl = from.icons.getOrNull(0)?.url ?: DEFAULT_ICON_URL
    )

    companion object {
        private const val DEFAULT_ICON_URL = "https://t.scdn.co/media/derived/r-b-274x274_fd56efa72f4f63764b011b68121581d8_0_0_274_274.jpg"
    }
}