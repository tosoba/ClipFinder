package com.example.spotify.account.top.ui

import com.airbnb.mvrx.MvRxState
import com.example.core.android.model.PagedDataList
import com.example.core.android.model.spotify.Artist
import com.example.core.android.model.spotify.Track

data class SpotifyAccountTopState(
    val userLoggedIn: Boolean = false,
    val topTracks: PagedDataList<Track> = PagedDataList(),
    val artists: PagedDataList<Artist> = PagedDataList()
) : MvRxState
