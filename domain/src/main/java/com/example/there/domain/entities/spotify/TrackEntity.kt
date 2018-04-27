package com.example.there.domain.entities.spotify

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

    val query: String
        get() = "$name $albumName"
}

data class TrackArtistEntity(
        val id: String,
        val name: String
)