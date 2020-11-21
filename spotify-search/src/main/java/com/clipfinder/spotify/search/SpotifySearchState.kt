package com.clipfinder.spotify.search

import com.airbnb.mvrx.MvRxState
import com.example.core.android.model.Empty
import com.example.core.android.model.Loadable
import com.example.core.android.model.PagedList
import com.example.core.android.spotify.model.Album
import com.example.core.android.spotify.model.Artist
import com.example.core.android.spotify.model.Playlist
import com.example.core.android.spotify.model.Track

data class SpotifySearchState(
    val query: String,
    val albums: Loadable<PagedList<Album>>,
    val artists: Loadable<PagedList<Artist>>,
    val playlists: Loadable<PagedList<Playlist>>,
    val tracks: Loadable<PagedList<Track>>
) : MvRxState {
    constructor(query: String) : this(query, Empty, Empty, Empty, Empty)
}
