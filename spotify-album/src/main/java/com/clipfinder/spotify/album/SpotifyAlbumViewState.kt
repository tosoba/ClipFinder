package com.clipfinder.spotify.album

import com.airbnb.mvrx.MvRxState
import com.clipfinder.core.android.spotify.model.Album
import com.clipfinder.core.android.spotify.model.Artist
import com.clipfinder.core.android.spotify.model.Track
import com.clipfinder.core.model.Empty
import com.clipfinder.core.model.Loadable
import com.clipfinder.core.model.PagedList

data class SpotifyAlbumViewState(
    val album: Album,
    val artists: Loadable<List<Artist>>,
    val tracks: Loadable<PagedList<Track>>
) : MvRxState {
    constructor(argAlbum: Album) : this(argAlbum, Empty, Empty)
}
