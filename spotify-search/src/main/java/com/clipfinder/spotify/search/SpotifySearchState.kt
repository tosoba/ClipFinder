package com.clipfinder.spotify.search

import com.airbnb.mvrx.MvRxState
import com.example.core.android.model.Empty
import com.example.core.android.model.Loadable
import com.example.core.android.model.PagedItemsList
import com.example.core.android.spotify.model.Album
import com.example.core.android.spotify.model.Artist
import com.example.core.android.spotify.model.Playlist
import com.example.core.android.spotify.model.Track

data class SpotifySearchState(
    val query: String,
    val albums: Loadable<PagedItemsList<Album>>,
    val artists: Loadable<PagedItemsList<Artist>>,
    val playlists: Loadable<PagedItemsList<Playlist>>,
    val tracks: Loadable<PagedItemsList<Track>>
) : MvRxState {
    constructor(query: String) : this(query, Empty, Empty, Empty, Empty)
}
