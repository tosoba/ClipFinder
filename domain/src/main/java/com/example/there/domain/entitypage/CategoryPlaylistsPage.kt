package com.example.there.domain.entitypage

import com.example.there.domain.entity.spotify.PlaylistEntity

data class CategoryPlaylistsPage(
        val playlists: List<PlaylistEntity>,
        val offset: Int,
        val totalItems: Int
)