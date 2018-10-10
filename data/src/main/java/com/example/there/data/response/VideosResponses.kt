package com.example.there.data.response

import com.example.there.data.entity.videos.ChannelData
import com.example.there.data.entity.videos.VideoData
import com.example.there.data.entity.videos.VideoSearchData
import com.google.gson.annotations.SerializedName

class VideosResponse(
        @SerializedName("items") val videos: List<VideoData>
)

class VideosSearchResponse(
        val nextPageToken: String?,
        @SerializedName("items") val videos: List<VideoSearchData>
)

class ChannelsResponse(
        @SerializedName("items") val channels: List<ChannelData>
)