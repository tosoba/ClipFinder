package com.clipfinder.spotify.account.playlist

import com.airbnb.mvrx.MvRxState
import com.clipfinder.core.android.spotify.model.Playlist
import com.clipfinder.core.model.Empty
import com.clipfinder.core.model.Loadable
import com.clipfinder.core.model.PagedList

data class SpotifyAccountPlaylistState(
    val userLoggedIn: Boolean = false,
    val playlists: Loadable<PagedList<Playlist>> = Empty
) : MvRxState
