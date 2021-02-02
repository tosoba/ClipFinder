package com.clipfinder.spotify.dashboard

import com.airbnb.mvrx.MvRxState
import com.clipfinder.core.model.Empty
import com.clipfinder.core.model.Loadable
import com.clipfinder.core.model.PagedList
import com.clipfinder.core.android.spotify.model.Album
import com.clipfinder.core.android.spotify.model.Category
import com.clipfinder.core.android.spotify.model.Playlist
import com.clipfinder.core.android.spotify.model.TopTrack

data class SpotifyDashboardState(
    val categories: Loadable<PagedList<Category>> = Empty,
    val featuredPlaylists: Loadable<PagedList<Playlist>> = Empty,
    val viralTracks: Loadable<PagedList<TopTrack>> = Empty,
    val newReleases: Loadable<PagedList<Album>> = Empty
) : MvRxState
