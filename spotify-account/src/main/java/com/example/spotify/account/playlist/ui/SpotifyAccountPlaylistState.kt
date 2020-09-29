package com.example.spotify.account.playlist.ui

import com.airbnb.mvrx.MvRxState
import com.example.core.android.model.PagedDataList
import com.example.core.android.spotify.model.Playlist

data class SpotifyAccountPlaylistState(
    val userLoggedIn: Boolean = false,
    val playlists: PagedDataList<Playlist> = PagedDataList()
) : MvRxState
