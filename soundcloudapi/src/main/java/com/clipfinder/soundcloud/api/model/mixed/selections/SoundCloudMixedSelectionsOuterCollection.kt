package com.clipfinder.soundcloud.api.model.mixed.selections

import com.google.gson.annotations.SerializedName

data class SoundCloudMixedSelectionsOuterCollection(
    val description: String,
    val id: String,
    val items: SoundCloudMixedSelectionsItems,
    val kind: String,
    @SerializedName("query_urn")
    val queryUrn: String,
    val title: String,
    @SerializedName("tracking_feature_name")
    val trackingFeatureName: String,
    val urn: String
)