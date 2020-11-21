package com.clipfinder.spotify.album

import com.airbnb.mvrx.MvRxState
import com.example.core.android.model.Empty
import com.example.core.android.model.Loadable
import com.example.core.android.model.PagedList
import com.example.core.android.spotify.model.Album
import com.example.core.android.spotify.model.Artist
import com.example.core.android.spotify.model.Track

data class SpotifyAlbumViewState(
    val album: Album,
    val artists: Loadable<List<Artist>>,
    val tracks: Loadable<PagedList<Track>>
) : MvRxState {
    constructor(argAlbum: Album) : this(argAlbum, Empty, Empty)
}
