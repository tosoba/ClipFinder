package com.clipfinder.spotify.playlist

import com.airbnb.mvrx.MvRxState
import com.example.core.android.model.Empty
import com.example.core.android.model.Loadable
import com.example.core.android.model.PagedList
import com.example.core.android.spotify.model.Playlist
import com.example.core.android.spotify.model.Track

data class SpotifyPlaylistState(
    val playlist: Playlist,
    val tracks: Loadable<PagedList<Track>>,
    val isSavedAsFavourite: Loadable<Boolean>
) : MvRxState {
    constructor(playlist: Playlist) : this(playlist, Empty, Empty)
}
