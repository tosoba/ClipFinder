package com.example.there.domain.entitypage

import com.example.there.domain.entity.spotify.AlbumEntity

data class AlbumsPage(
        val albums: List<AlbumEntity>,
        val offset: Int,
        val totalItems: Int
)