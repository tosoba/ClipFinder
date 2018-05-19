package com.example.there.data.mappers.videos

import com.example.there.data.entities.videos.ChannelData
import com.example.there.data.util.urlMedium
import com.example.there.domain.common.OneWayMapper

object ChannelThumbnailUrlMapper : OneWayMapper<ChannelData, String>() {
    override fun mapFrom(from: ChannelData): String = from.snippet.thumbnails.urlMedium
}