package com.clipfinder.soundcloud.api.model.mixed.selections

import com.google.gson.annotations.SerializedName

data class SoundCloudMixedSelectionsResponse(
    val collection: List<SoundCloudMixedSelectionsOuterCollection>,
    @SerializedName("next_href") val nextHref: String?,
    @SerializedName("query_urn") val queryUrn: String
)
