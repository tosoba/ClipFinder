package com.example.spotify.account.playlist

import com.airbnb.mvrx.MvRxState
import com.example.core.android.model.Empty
import com.example.core.android.model.Loadable
import com.example.core.android.model.PagedList
import com.example.core.android.spotify.model.Playlist

data class SpotifyAccountPlaylistState(
    val userLoggedIn: Boolean = false,
    val playlists: Loadable<PagedList<Playlist>> = Empty
) : MvRxState
