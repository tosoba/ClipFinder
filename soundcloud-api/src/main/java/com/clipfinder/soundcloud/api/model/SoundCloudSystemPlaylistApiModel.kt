package com.clipfinder.soundcloud.api.model

import com.google.gson.annotations.SerializedName

class SoundCloudSystemPlaylistApiModel(
    @SerializedName("artwork_url")
    val artworkUrl: String?,

    @SerializedName("calculated_artwork_url")
    val calculatedArtworkUrl: String?,

    val description: String,

    val id: String,

    @SerializedName("kind")
    val kind: String,

    @SerializedName("last_updated")
    val lastUpdated: String,

    val permalink: String,

    @SerializedName("short_description")
    val shortDescription: String,

    @SerializedName("short_title")
    val shortTitle: String,

    val title: String,

    @SerializedName("tracking_feature_name")
    val trackingFeatureName: String,

    val tracks: List<SoundCloudTrackId>
)
