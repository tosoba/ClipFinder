package com.example.there.domain.entities.spotify

data class SearchAllEntity(
        val albums: List<AlbumEntity>,
        val artists: List<ArtistEntity>,
        val playlists: List<PlaylistEntity>,
        val tracks: List<TrackEntity>,
        val albumsNextPageUrl: String?,
        val artistsNextPageUrl: String?,
        val playlistsNextPageUrl: String?,
        val tracksNextPageUrl: String?
)