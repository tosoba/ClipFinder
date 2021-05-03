package com.clipfinder.soundcloud.api.model

import com.google.gson.annotations.SerializedName

class SoundCloudUser(
    @SerializedName("avatar_url") val avatarUrl: String,
    @SerializedName("first_name") val firstName: String,
    @SerializedName("full_name") val fullName: String,
    val id: Int,
    val kind: String,
    @SerializedName("last_modified") val lastModified: String,
    @SerializedName("last_name") val lastName: String,
    val permalink: String,
    @SerializedName("permalink_url") val permalinkUrl: String,
    @SerializedName("uri") val uri: String,
    @SerializedName("urn") val urn: String,
    @SerializedName("username") val username: String,
    @SerializedName("verified") val verified: Boolean
)
