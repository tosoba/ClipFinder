package com.clipfinder.soundcloud.api.model.mixed.selections

import com.google.gson.annotations.SerializedName

data class SoundCloudMixedSelectionTrack(
    val id: Int,
    val kind: String,
    @SerializedName("monetization_model") val monetizationModel: String,
    val policy: String
)
