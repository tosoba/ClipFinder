package com.clipfinder.spotify.album

import com.airbnb.mvrx.MvRxState
import com.example.core.android.model.DefaultLoadable
import com.example.core.android.model.DefaultReady
import com.example.core.android.model.PagedItemsList
import com.example.core.android.spotify.model.Album
import com.example.core.android.spotify.model.Artist
import com.example.core.android.spotify.model.Track

data class SpotifyAlbumViewState(
    val album: Album,
    val artists: DefaultLoadable<List<Artist>> = DefaultReady(emptyList()),
    val tracks: DefaultLoadable<PagedItemsList<Track>> = DefaultReady(PagedItemsList())
) : MvRxState {
    constructor(argAlbum: Album) : this(
        argAlbum,
        DefaultReady(emptyList()),
        DefaultReady(PagedItemsList())
    )
}
