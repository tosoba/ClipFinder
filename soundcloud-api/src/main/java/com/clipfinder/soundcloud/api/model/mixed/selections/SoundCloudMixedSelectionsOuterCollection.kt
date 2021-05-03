package com.clipfinder.soundcloud.api.model.mixed.selections

import com.clipfinder.core.soundcloud.model.ISoundCloudPlaylist
import com.clipfinder.core.soundcloud.model.ISoundCloudPlaylistSelection
import com.google.gson.annotations.SerializedName

data class SoundCloudMixedSelectionsOuterCollection(
    override val description: String?,
    override val id: String,
    val items: SoundCloudMixedSelectionsItems,
    val kind: String,
    @SerializedName("query_urn") val queryUrn: String,
    override val title: String,
    @SerializedName("tracking_feature_name") val trackingFeatureName: String,
    val urn: String
) : ISoundCloudPlaylistSelection {
    override val playlists: List<ISoundCloudPlaylist>
        get() = items.collection
}
