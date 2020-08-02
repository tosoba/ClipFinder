package com.example.spotifysearch.domain.model

import com.example.core.model.Paged
import com.example.there.domain.entity.spotify.AlbumEntity
import com.example.there.domain.entity.spotify.ArtistEntity
import com.example.there.domain.entity.spotify.PlaylistEntity
import com.example.there.domain.entity.spotify.TrackEntity

data class SpotifySearchResult(
    val albums: Paged<List<AlbumEntity>>?,
    val artists: Paged<List<ArtistEntity>>?,
    val playlists: Paged<List<PlaylistEntity>>?,
    val tracks: Paged<List<TrackEntity>>?
)
