package com.clipfinder.spotify.dashboard

import com.airbnb.mvrx.MvRxState
import com.example.core.android.model.Empty
import com.example.core.android.model.Loadable
import com.example.core.android.model.PagedList
import com.example.core.android.spotify.model.Album
import com.example.core.android.spotify.model.Category
import com.example.core.android.spotify.model.Playlist
import com.example.core.android.spotify.model.TopTrack

data class SpotifyDashboardState(
    val categories: Loadable<PagedList<Category>> = Empty,
    val featuredPlaylists: Loadable<PagedList<Playlist>> = Empty,
    val viralTracks: Loadable<PagedList<TopTrack>> = Empty,
    val newReleases: Loadable<PagedList<Album>> = Empty
) : MvRxState
