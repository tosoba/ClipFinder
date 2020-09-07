package com.example.there.domain.entity.spotify

data class PlaylistEntity(
    val id: String,
    val name: String,
    val iconUrl: String,
    val userId: String?,
    val uri: String
)
