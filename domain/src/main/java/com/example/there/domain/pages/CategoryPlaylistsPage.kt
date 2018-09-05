package com.example.there.domain.pages

import com.example.there.domain.entities.spotify.PlaylistEntity

data class CategoryPlaylistsPage(
        val playlists: List<PlaylistEntity>,
        val offset: Int,
        val totalItems: Int
)