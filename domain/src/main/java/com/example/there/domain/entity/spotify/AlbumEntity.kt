package com.example.there.domain.entity.spotify

data class AlbumEntity(
        val id: String,
        val name: String,
        val artists: List<SimpleArtistEntity>,
        val albumType: String,
        val iconUrl: String
)