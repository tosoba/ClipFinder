package com.clipfinder.soundcloud.api.model.front

import com.google.gson.annotations.SerializedName

data class SoundCloudFrontResponse(
    val collection: List<SoundCloudFrontCollection>,
    @SerializedName("next_href")
    val nextHref: String?,
    @SerializedName("query_urn")
    val queryUrn: String?
)