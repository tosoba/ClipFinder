package com.example.spotify.account.saved.ui

import com.airbnb.mvrx.MvRxState
import com.example.core.android.model.PagedDataList
import com.example.core.android.spotify.model.Album
import com.example.core.android.spotify.model.Track

data class SpotifyAccountSavedState(
    val userLoggedIn: Boolean = false,
    val tracks: PagedDataList<Track> = PagedDataList(),
    val albums: PagedDataList<Album> = PagedDataList()
) : MvRxState
