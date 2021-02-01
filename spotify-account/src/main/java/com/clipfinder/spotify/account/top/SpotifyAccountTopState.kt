package com.clipfinder.spotify.account.top

import com.airbnb.mvrx.MvRxState
import com.clipfinder.core.model.Empty
import com.clipfinder.core.model.Loadable
import com.clipfinder.core.model.PagedList
import com.example.core.android.spotify.model.Artist
import com.example.core.android.spotify.model.Track

data class SpotifyAccountTopState(
    val userLoggedIn: Boolean = false,
    val topTracks: Loadable<PagedList<Track>> = Empty,
    val artists: Loadable<PagedList<Artist>> = Empty
) : MvRxState
