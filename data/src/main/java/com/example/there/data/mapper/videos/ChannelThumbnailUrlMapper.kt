package com.example.there.data.mapper.videos

import com.example.there.data.entities.videos.ChannelData
import com.example.there.data.util.urlMedium
import com.example.there.domain.common.Mapper

object ChannelThumbnailUrlMapper : Mapper<ChannelData, String>() {
    override fun mapFrom(from: ChannelData): String = from.snippet.thumbnails.urlMedium
}