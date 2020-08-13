package com.example.spotifyaccount.playlist.ui

import com.airbnb.mvrx.MvRxState
import com.example.core.android.model.PagedDataList
import com.example.core.android.model.spotify.Playlist

data class AccountPlaylistState(
    val userLoggedIn: Boolean = false,
    val playlists: PagedDataList<Playlist> = PagedDataList()
) : MvRxState
