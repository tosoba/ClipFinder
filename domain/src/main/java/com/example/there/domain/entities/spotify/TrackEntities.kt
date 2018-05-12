package com.example.there.domain.entities.spotify

data class TrackEntity(
        val id: String,
        val name: String,
        val iconUrl: String,
        val albumId: String,
        val albumName: String,
        val artists: List<SimpleArtistEntity>
)

data class TopTrackEntity(
        val position: Int,
        val track: TrackEntity
)

data class SimilarTrackEntity(
        val id: String,
        val name: String,
        val artists: List<SimpleArtistEntity>
)