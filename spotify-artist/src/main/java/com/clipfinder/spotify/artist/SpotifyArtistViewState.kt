package com.clipfinder.spotify.artist

import com.airbnb.mvrx.MvRxState
import com.example.core.android.model.DefaultLoadable
import com.example.core.android.model.DefaultReady
import com.example.core.android.model.PagedItemsList
import com.example.core.android.spotify.model.Album
import com.example.core.android.spotify.model.Artist
import com.example.core.android.spotify.model.Track

data class SpotifyArtistViewState(
    val artists: List<Artist> = emptyList(),
    val albums: DefaultLoadable<PagedItemsList<Album>> = DefaultReady(PagedItemsList()),
    val topTracks: DefaultLoadable<List<Track>> = DefaultReady(emptyList()),
    val relatedArtists: DefaultLoadable<List<Artist>> = DefaultReady(emptyList()),
    val isSavedAsFavourite: DefaultLoadable<Boolean> = DefaultReady(false)
) : MvRxState {
    constructor(argArtist: Artist) : this(listOf(argArtist))
}
