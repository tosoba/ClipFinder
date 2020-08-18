package com.clipfinder.soundcloud.api.model.mixed.selections

data class SoundCloudMixedSelectionsResponse(
    val collection: List<SoundCloudMixedSelectionsOuterCollection>,
    val next_href: String?,
    val query_urn: String
)