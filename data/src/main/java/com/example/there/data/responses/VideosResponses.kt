package com.example.there.data.responses

import com.example.there.data.entities.videos.ChannelData
import com.example.there.data.entities.videos.VideoData
import com.example.there.data.entities.videos.VideoSearchData
import com.google.gson.annotations.SerializedName

data class VideosResponse(
        @SerializedName("items") val videos: List<VideoData>
)

data class VideosSearchResponse(
        val nextPageToken: String?,
        @SerializedName("items") val videos: List<VideoSearchData>
)

data class ChannelsResponse(
        @SerializedName("items") val channels: List<ChannelData>
)