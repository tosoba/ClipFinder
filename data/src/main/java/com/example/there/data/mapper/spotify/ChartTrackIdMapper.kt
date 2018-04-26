package com.example.there.data.mapper.spotify

import com.example.there.domain.common.Mapper
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ChartTrackIdMapper @Inject constructor() : Mapper<String, String>() {
    override fun mapFrom(from: String): String = from.substring(from.lastIndexOf('/') + 1)
}