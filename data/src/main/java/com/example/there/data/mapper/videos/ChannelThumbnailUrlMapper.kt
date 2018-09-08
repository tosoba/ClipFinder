package com.example.there.data.mapper.videos

import com.example.there.data.entity.videos.ChannelData
import com.example.there.data.util.urlMedium
import com.example.there.domain.common.OneWayMapper

object ChannelThumbnailUrlMapper : OneWayMapper<ChannelData, String>() {
    override fun mapFrom(from: ChannelData): String = from.snippet.thumbnails.urlMedium
}