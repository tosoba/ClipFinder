package com.clipfinder.spotify.dashboard

import com.airbnb.mvrx.MvRxState
import com.example.core.android.model.*
import com.example.core.android.spotify.model.Album
import com.example.core.android.spotify.model.Category
import com.example.core.android.spotify.model.Playlist
import com.example.core.android.spotify.model.TopTrack

data class SpotifyDashboardState(
    val categories: Loadable<PagedItemsList<Category>> = Empty,
    val featuredPlaylists: DefaultLoadable<PagedItemsList<Playlist>> = DefaultReady(PagedItemsList()),
    val viralTracks: DefaultLoadable<PagedItemsList<TopTrack>> = DefaultReady(PagedItemsList()),
    val newReleases: DefaultLoadable<PagedItemsList<Album>> = DefaultReady(PagedItemsList())
) : MvRxState
