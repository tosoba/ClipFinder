package com.example.spotifyaccount.saved.ui

import com.airbnb.mvrx.MvRxState
import com.example.core.android.model.PagedDataList
import com.example.core.android.model.spotify.Album
import com.example.core.android.model.spotify.Track

data class AccountSavedState(
    val userLoggedIn: Boolean = false,
    val tracks: PagedDataList<Track> = PagedDataList(),
    val albums: PagedDataList<Album> = PagedDataList()
) : MvRxState
