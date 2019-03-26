package com.example.api.model

import com.google.gson.annotations.SerializedName

class DiscoverResponse(
        val collection: List<SoundCloudCollectionApiModel>
)

class SoundCloudRelatedTracksResponse(
        val collection: List<SoundCloudTrackApiModel>?,

        @SerializedName("next_href")
        val nextHref: String
)