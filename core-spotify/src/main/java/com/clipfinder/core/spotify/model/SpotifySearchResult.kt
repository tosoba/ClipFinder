package com.clipfinder.core.spotify.model

import com.example.core.model.Paged

data class SpotifySearchResult(
    val albums: Paged<List<ISpotifySimplifiedAlbum>>?,
    val artists: Paged<List<ISpotifyArtist>>?,
    val playlists: Paged<List<ISpotifySimplifiedPlaylist>>?,
    val tracks: Paged<List<ISpotifyTrack>>?
)
