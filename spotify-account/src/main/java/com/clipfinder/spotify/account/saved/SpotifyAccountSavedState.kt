package com.clipfinder.spotify.account.saved

import com.airbnb.mvrx.MvRxState
import com.example.core.android.model.Empty
import com.example.core.android.model.Loadable
import com.example.core.android.model.PagedList
import com.example.core.android.spotify.model.Album
import com.example.core.android.spotify.model.Track

data class SpotifyAccountSavedState(
    val userLoggedIn: Boolean = false,
    val tracks: Loadable<PagedList<Track>> = Empty,
    val albums: Loadable<PagedList<Album>> = Empty
) : MvRxState
