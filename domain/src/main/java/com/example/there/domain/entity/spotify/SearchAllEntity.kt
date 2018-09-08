package com.example.there.domain.entity.spotify

data class SearchAllEntity(
        val albums: List<AlbumEntity>,
        val artists: List<ArtistEntity>,
        val playlists: List<PlaylistEntity>,
        val tracks: List<TrackEntity>,
        val albumsTotalResults: Int,
        val albumsOffset: Int,
        val artistsTotalResults: Int,
        val artistsOffset: Int,
        val playlistsTotalResults: Int,
        val playlistsOffset: Int,
        val tracksTotalResults: Int,
        val tracksOffset: Int
)