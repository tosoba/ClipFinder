package com.example.there.findclips.mappers

import com.example.there.domain.common.Mapper
import com.example.there.domain.entities.spotify.CategoryEntity
import com.example.there.findclips.entities.Category

object CategoryEntityMapper: Mapper<CategoryEntity, Category>() {
    override fun mapFrom(from: CategoryEntity): Category = Category(
            id = from.id,
            name = from.name,
            iconUrl = from.iconUrl
    )
}