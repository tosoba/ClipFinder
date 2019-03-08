package com.example.there.data.entity.soundcloud

import com.google.gson.annotations.SerializedName

data class Collection(
        val description: String,
        val id: String,
        val kind: String,
        @SerializedName("last_updated") val lastUpdated: String,
        val playlists: List<SoundCloudPlaylist>?,
        @SerializedName("system_playlists") val systemPlaylists: List<SoundCloudSystemPlaylist>?,
        val title: String,
        @SerializedName("tracking_feature_name") val trackingFeatureName: String
)