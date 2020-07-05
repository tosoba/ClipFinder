package com.example.youtubeapi.model

import com.google.gson.annotations.SerializedName

class VideosResponse(@SerializedName("items") val videos: List<VideoApiModel>)

class VideosSearchResponse(
    val nextPageToken: String?,
    @SerializedName("items") val videos: List<VideoSearchApiModel>
)

class ChannelsResponse(@SerializedName("items") val channels: List<ChannelApiModel>)
