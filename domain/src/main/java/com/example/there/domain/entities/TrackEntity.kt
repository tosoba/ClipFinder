package com.example.there.domain.entities

data class TrackEntity(
        val id: String,
        val name: String,
        val iconUrl: String,
        val albumId: String,
        val albumName: String,
        val artists: List<TrackArtistEntity>
) {
    val artistNamesText: String
        get() = artists.joinToString(separator = ", ") { it.name }
}

data class TrackArtistEntity(
        val id: String,
        val name: String
)