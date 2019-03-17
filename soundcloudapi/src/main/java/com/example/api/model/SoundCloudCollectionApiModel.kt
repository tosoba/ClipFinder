package com.example.api.model

import com.google.gson.annotations.SerializedName

class SoundCloudCollectionApiModel(
        val description: String,

        val id: String,

        val kind: String,

        @SerializedName("last_updated")
        val lastUpdated: String,

        val playlists: List<SoundCloudPlaylistApiModel>?,

        @SerializedName("system_playlists")
        val systemPlaylists: List<SoundCloudSystemPlaylistApiModel>?,

        val title: String,

        @SerializedName("tracking_feature_name")
        val trackingFeatureName: String
)