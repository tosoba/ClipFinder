package com.clipfinder.soundcloud.api.model.collection

import com.clipfinder.core.soundcloud.model.ISoundCloudTrack
import com.clipfinder.soundcloud.api.model.SoundCloudMedia
import com.clipfinder.soundcloud.api.model.SoundCloudUser
import com.google.gson.annotations.SerializedName

data class SoundCloudCollectionItem(
    @SerializedName("artwork_url") override val artworkUrl: String,
    @SerializedName("comment_count") val commentCount: Int,
    val commentable: Boolean,
    @SerializedName("created_at") val createdAt: String,
    override val description: String,
    @SerializedName("display_date") val displayDate: String,
    @SerializedName("download_count") val downloadCount: Int,
    val downloadable: Boolean,
    override val duration: Int,
    @SerializedName("embeddable_by") val embeddableBy: String,
    @SerializedName("full_duration") val fullDuration: Int,
    override val genre: String,
    @SerializedName("has_downloads_left") val hasDownloadsLeft: Boolean,
    override val id: Int,
    val kind: String,
    @SerializedName("label_name") val labelName: Any,
    @SerializedName("last_modified") val lastModified: String,
    val license: String,
    @SerializedName("likes_count") val likesCount: Int,
    val media: SoundCloudMedia,
    @SerializedName("monetization_model") val monetizationModel: String,
    val permalink: String,
    @SerializedName("permalink_url") val permalinkUrl: String,
    @SerializedName("playback_count") val playbackCount: Int,
    val policy: String,
    val `public`: Boolean,
    @SerializedName("publisher_metadata") val publisherMetadata: SoundCloudPublisherMetadata,
    @SerializedName("reposts_count") val repostsCount: Int,
    val sharing: String,
    val state: String,
    val streamable: Boolean,
    @SerializedName("tag_list") val tagList: String,
    override val title: String,
    val uri: String,
    val urn: String,
    val user: SoundCloudUser,
    @SerializedName("user_id") val userId: Int,
    @SerializedName("waveform_url") override val waveformUrl: String
) : ISoundCloudTrack {
    override val tags: String
        get() = tagList
    override val streamUrl: String?
        get() =
            media.transcodings
                ?.find {
                    it.preset == "mp3_0_1" &&
                        it.format.mimeType == "audio/mpeg" &&
                        it.format.protocol == "hls"
                }
                ?.url
}
