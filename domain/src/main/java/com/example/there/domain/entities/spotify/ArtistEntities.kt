package com.example.there.domain.entities.spotify

data class ArtistEntity(
        val id: String,
        val name: String,
        val popularity: Int,
        val iconUrl: String
)

data class SimpleArtistEntity(
        val id: String,
        val name: String
)