package com.example.there.findclips.model.mapper

import com.example.there.domain.common.TwoWayMapper
import com.example.there.domain.entity.spotify.CategoryEntity
import com.example.there.findclips.model.entity.Category

object CategoryEntityMapper: TwoWayMapper<CategoryEntity, Category>() {
    override fun mapFrom(from: CategoryEntity): Category = Category(
            id = from.id,
            name = from.name,
            iconUrl = from.iconUrl
    )

    override fun mapBack(from: Category): CategoryEntity = CategoryEntity(
            id = from.id,
            name = from.name,
            iconUrl = from.iconUrl
    )
}