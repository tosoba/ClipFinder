package com.example.there.findclips.model.mappers

import com.example.there.domain.common.TwoWayMapper
import com.example.there.domain.entities.spotify.CategoryEntity
import com.example.there.findclips.model.entities.Category

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