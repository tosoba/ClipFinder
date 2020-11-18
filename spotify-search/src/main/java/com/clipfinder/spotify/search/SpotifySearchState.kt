package com.clipfinder.spotify.search

import com.airbnb.mvrx.MvRxState
import com.example.core.android.model.DefaultLoadable
import com.example.core.android.model.DefaultReady
import com.example.core.android.model.PagedItemsList
import com.example.core.android.spotify.model.Album
import com.example.core.android.spotify.model.Artist
import com.example.core.android.spotify.model.Playlist
import com.example.core.android.spotify.model.Track

data class SpotifySearchState(
    val query: String,
    val albums: DefaultLoadable<PagedItemsList<Album>> = DefaultReady(PagedItemsList()),
    val artists: DefaultLoadable<PagedItemsList<Artist>> = DefaultReady(PagedItemsList()),
    val playlists: DefaultLoadable<PagedItemsList<Playlist>> = DefaultReady(PagedItemsList()),
    val tracks: DefaultLoadable<PagedItemsList<Track>> = DefaultReady(PagedItemsList())
) : MvRxState {
    constructor(query: String) : this(
        query,
        DefaultReady(PagedItemsList()),
        DefaultReady(PagedItemsList()),
        DefaultReady(PagedItemsList()),
        DefaultReady(PagedItemsList())
    )
}
