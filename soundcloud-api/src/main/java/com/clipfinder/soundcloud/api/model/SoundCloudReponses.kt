package com.clipfinder.soundcloud.api.model

import com.google.gson.annotations.SerializedName

class DiscoverResponse(
    val collection: List<SoundCloudCollectionApiModel>
)

class SoundCloudRelatedTracksResponse(
    val collection: List<SoundCloudTrack>?,

    @SerializedName("next_href")
    val nextHref: String
)
