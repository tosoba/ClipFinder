package com.example.there.data.response.videos

import com.example.there.data.entities.videos.VideoData
import com.google.gson.annotations.SerializedName

data class VideosResponse(
        @SerializedName("items") val videos: List<VideoData>
)