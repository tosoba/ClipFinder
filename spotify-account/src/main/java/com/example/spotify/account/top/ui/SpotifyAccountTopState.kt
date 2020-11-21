package com.example.spotify.account.top.ui

import com.airbnb.mvrx.MvRxState
import com.example.core.android.model.Empty
import com.example.core.android.model.Loadable
import com.example.core.android.model.PagedItemsList
import com.example.core.android.spotify.model.Artist
import com.example.core.android.spotify.model.Track

data class SpotifyAccountTopState(
    val userLoggedIn: Boolean = false,
    val topTracks: Loadable<PagedItemsList<Track>> = Empty,
    val artists: Loadable<PagedItemsList<Artist>> = Empty
) : MvRxState
