package com.example.there.domain.entity.spotify

data class TrackEntity(
        val id: String,
        val name: String,
        val iconUrl: String,
        val albumId: String,
        val albumName: String,
        val artists: List<SimpleArtistEntity>,
        val popularity: Int,
        val trackNumber: Int
)

data class TopTrackEntity(
        val position: Int,
        val track: TrackEntity
)