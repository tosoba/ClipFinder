package com.clipfinder.spotify.search

import com.airbnb.mvrx.MvRxState
import com.clipfinder.core.android.spotify.model.Album
import com.clipfinder.core.android.spotify.model.Artist
import com.clipfinder.core.android.spotify.model.Playlist
import com.clipfinder.core.android.spotify.model.Track
import com.clipfinder.core.model.Empty
import com.clipfinder.core.model.Loadable
import com.clipfinder.core.model.PagedList

data class SpotifySearchState(
    val query: String,
    val albums: Loadable<PagedList<Album>>,
    val artists: Loadable<PagedList<Artist>>,
    val playlists: Loadable<PagedList<Playlist>>,
    val tracks: Loadable<PagedList<Track>>
) : MvRxState {
    constructor(query: String) : this(query, Empty, Empty, Empty, Empty)
}
