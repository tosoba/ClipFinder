package com.example.api.model

import com.google.gson.annotations.SerializedName

class SoundCloudPlaylistApiModel(
        @SerializedName("artwork_url")
        val artworkUrl: String?,

        @SerializedName("created_at")
        val createdAt: String,

        @SerializedName("display_date")
        val displayDate: String,

        val duration: Int,

        val id: Int,

        @SerializedName("is_album")
        val isAlbum: Boolean,

        val kind: String,

        @SerializedName("likes_count")
        val likesCount: Int,

        val permalink: String,

        @SerializedName("permalink_url")
        val permalinkUrl: String,

        @SerializedName("public")
        val `public`: Boolean,

        @SerializedName("published_at")
        val publishedAt: String?,

        @SerializedName("reposts_count")
        val repostsCount: Int,

        val title: String,

        @SerializedName("track_count")
        val trackCount: Int,

        val uri: String,

        val user: SoundCloudUserApiModel,

        @SerializedName("user_id")
        val userId: Int
)