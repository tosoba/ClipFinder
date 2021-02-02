package com.clipfinder.spotify.account.saved

import com.airbnb.mvrx.MvRxState
import com.clipfinder.core.model.Empty
import com.clipfinder.core.model.Loadable
import com.clipfinder.core.model.PagedList
import com.clipfinder.core.android.spotify.model.Album
import com.clipfinder.core.android.spotify.model.Track

data class SpotifyAccountSavedState(
    val userLoggedIn: Boolean = false,
    val tracks: Loadable<PagedList<Track>> = Empty,
    val albums: Loadable<PagedList<Album>> = Empty
) : MvRxState
