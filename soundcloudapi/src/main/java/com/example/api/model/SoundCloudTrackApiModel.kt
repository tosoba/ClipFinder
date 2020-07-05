package com.example.api.model

import com.google.gson.annotations.SerializedName

class SoundCloudTrackApiModel(
    @SerializedName("full_duration")
    val fullDuration: Int,

    val downloadable: Boolean,

    @SerializedName("created_at")
    val createdAt: String,

    val description: String,

    val media: Media,

    val title: String,

    val duration: Int,

    @SerializedName("has_downloads_left")
    val hasDownloadsLeft: Boolean,

    @SerializedName("artwork_url")
    val artworkUrl: String?,

    val public: Boolean,

    val streamable: Boolean,

    @SerializedName("tag_list")
    val tagList: String,

    val genre: String,

    val id: Int,

    @SerializedName("reposts_count")
    val repostsCount: Int,

    val state: String,

    @SerializedName("label_name")
    val labelName: String,

    @SerializedName("last_modified")
    val lastModified: String,

    val commentable: Boolean,

    val policy: String,

    val kind: String,

    val sharing: String,

    val uri: String,

    @SerializedName("likes_count")
    val likesCount: Int,

    val urn: String,

    val license: String,

    @SerializedName("display_date")
    val displayDate: String,

    @SerializedName("embeddable_by")
    val embeddableBy: String,

    @SerializedName("user_id")
    val userId: Int,

    @SerializedName("monetization_model")
    val monetizationModel: String,

    @SerializedName("waveform_url")
    val waveformUrl: String,

    val permalink: String,

    @SerializedName("permalink_url")
    val permalinkUrl: String,

    @SerializedName("user")
    val user: SoundCloudUserApiModel
)
