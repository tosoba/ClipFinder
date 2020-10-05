package com.example.spotify.search.domain.model

import com.clipfinder.core.spotify.model.ISpotifyArtist
import com.clipfinder.core.spotify.model.ISpotifySimplifiedAlbum
import com.clipfinder.core.spotify.model.ISpotifySimplifiedPlaylist
import com.clipfinder.core.spotify.model.ISpotifyTrack
import com.example.core.model.Paged

data class SpotifySearchResult(
    val albums: Paged<List<ISpotifySimplifiedAlbum>>?,
    val artists: Paged<List<ISpotifyArtist>>?,
    val playlists: Paged<List<ISpotifySimplifiedPlaylist>>?,
    val tracks: Paged<List<ISpotifyTrack>>?
)
