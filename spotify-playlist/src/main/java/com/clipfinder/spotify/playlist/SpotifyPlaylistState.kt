package com.clipfinder.spotify.playlist

import com.airbnb.mvrx.MvRxState
import com.clipfinder.core.model.Empty
import com.clipfinder.core.model.Loadable
import com.clipfinder.core.model.PagedList
import com.example.core.android.spotify.model.Playlist
import com.example.core.android.spotify.model.Track

data class SpotifyPlaylistState(
    val playlist: Playlist,
    val tracks: Loadable<PagedList<Track>>,
    val isSavedAsFavourite: Loadable<Boolean>
) : MvRxState {
    constructor(playlist: Playlist) : this(playlist, Empty, Empty)
}
