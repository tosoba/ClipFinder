package com.example.there.domain.entity.spotify

data class SearchAllEntity(
        val albums: List<AlbumEntity>,
        val artists: List<ArtistEntity>,
        val playlists: List<PlaylistEntity>,
        val tracks: List<TrackEntity>,
        val totalItems: Int
)