package com.example.there.domain.entity.soundcloud

data class SoundCloudUserEntity(
        val avatarUrl: String,
        val firstName: String,
        val fullName: String,
        val id: Int,
        val kind: String,
        val lastModified: String,
        val lastName: String,
        val permalink: String,
        val permalinkUrl: String,
        val uri: String,
        val urn: String,
        val username: String,
        val verified: Boolean
)