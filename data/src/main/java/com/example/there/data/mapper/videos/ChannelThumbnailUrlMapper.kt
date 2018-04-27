package com.example.there.data.mapper.videos

import com.example.there.data.entities.videos.ChannelData
import com.example.there.data.util.urlMedium
import com.example.there.domain.common.Mapper
import javax.inject.Singleton
import javax.inject.Inject

@Singleton
class ChannelThumbnailUrlMapper @Inject constructor(): Mapper<ChannelData, String>() {
    override fun mapFrom(from: ChannelData): String = from.snippet.thumbnails.urlMedium
}