package com.clipfinder.spotify.artist

import com.airbnb.mvrx.MvRxState
import com.example.core.android.model.Loadable
import com.example.core.android.model.PagedItemsList
import com.example.core.android.model.Ready
import com.example.core.android.spotify.model.Album
import com.example.core.android.spotify.model.Artist
import com.example.core.android.spotify.model.Track

data class SpotifyArtistViewState(
    val artists: List<Artist> = emptyList(),
    val albums: Loadable<PagedItemsList<Album>> = Ready(PagedItemsList()),
    val topTracks: Loadable<List<Track>> = Ready(emptyList()),
    val relatedArtists: Loadable<List<Artist>> = Ready(emptyList()),
    val isSavedAsFavourite: Loadable<Boolean> = Ready(false)
) : MvRxState {
    constructor(argArtist: Artist) : this(listOf(argArtist))
}
