package com.clipfinder.soundcloud.api.model.collection

import com.google.gson.annotations.SerializedName

data class SoundCollectionResponse(
    val collection: List<SoundCloudCollectionItem>,
    @SerializedName("next_href")
    val nextHref: String?,
    @SerializedName("query_urn")
    val queryUrn: String?
)