package com.example.there.data.mappers.spotify

import com.example.there.domain.common.Mapper

object ChartTrackIdMapper : Mapper<String, String>() {
    override fun mapFrom(from: String): String = from.substring(from.lastIndexOf('/') + 1)
}