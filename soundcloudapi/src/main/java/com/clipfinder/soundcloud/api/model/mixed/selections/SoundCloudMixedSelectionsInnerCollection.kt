package com.clipfinder.soundcloud.api.model.mixed.selections

import com.clipfinder.soundcloud.api.model.SoundCloudUser
import com.google.gson.annotations.SerializedName

data class SoundCloudMixedSelectionsInnerCollection(
    @SerializedName("artwork_url")
    val artworkUrl: String,
    @SerializedName("calculated_artwork_url")
    val calculatedArtworkUrl: String,
    @SerializedName("created_at")
    val createdAt: String,
    val description: String,
    @SerializedName("display_date")
    val displayDate: String,
    val duration: Int,
    val id: Int,
    @SerializedName("is_album")
    val isAlbum: Boolean,
    @SerializedName("is_public")
    val isPublic: Boolean,
    val kind: String,
    @SerializedName("last_modified")
    val lastModified: String,
    @SerializedName("last_updated")
    val lastUpdated: String,
    @SerializedName("likes_count")
    val likesCount: Int,
    @SerializedName("managed_by_feeds")
    val managedByFeeds: Boolean,
    val permalink: String,
    @SerializedName("permalink_url")
    val permalinkUrl: String,
    val `public`: Boolean,
    @SerializedName("published_at")
    val publishedAt: String,
    @SerializedName("reposts_count")
    val repostsCount: Int,
    @SerializedName("set_type")
    val setType: String,
    val sharing: String,
    @SerializedName("short_description")
    val shortDescription: String,
    @SerializedName("short_title")
    val shortTitle: String,
    val title: String,
    @SerializedName("track_count")
    val trackCount: Int,
    @SerializedName("tracking_feature_name")
    val trackingFeatureName: String,
    val tracks: List<SoundCloudMixedSelectionTrack>,
    val uri: String,
    val urn: String,
    val user: SoundCloudUser,
    @SerializedName("user_id")
    val userId: Int
)