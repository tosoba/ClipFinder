package com.clipfinder.spotify.dashboard

import com.airbnb.mvrx.MvRxState
import com.example.core.android.model.Empty
import com.example.core.android.model.Loadable
import com.example.core.android.model.PagedItemsList
import com.example.core.android.spotify.model.Album
import com.example.core.android.spotify.model.Category
import com.example.core.android.spotify.model.Playlist
import com.example.core.android.spotify.model.TopTrack

data class SpotifyDashboardState(
    val categories: Loadable<PagedItemsList<Category>> = Empty,
    val featuredPlaylists: Loadable<PagedItemsList<Playlist>> = Empty,
    val viralTracks: Loadable<PagedItemsList<TopTrack>> = Empty,
    val newReleases: Loadable<PagedItemsList<Album>> = Empty
) : MvRxState
