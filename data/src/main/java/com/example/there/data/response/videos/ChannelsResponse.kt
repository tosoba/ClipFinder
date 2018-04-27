package com.example.there.data.response.videos

import com.example.there.data.entities.videos.ChannelData
import com.google.gson.annotations.SerializedName

data class ChannelsResponse(
        @SerializedName("items") val channels: List<ChannelData>
)