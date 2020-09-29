package com.example.spotify.dashboard.ui

import com.airbnb.mvrx.MvRxState
import com.example.core.android.model.PagedDataList
import com.example.core.android.model.spotify.Album
import com.example.core.android.spotify.model.Category
import com.example.core.android.spotify.model.Playlist
import com.example.core.android.model.spotify.TopTrack

data class SpotifyDashboardState(
    val categories: PagedDataList<Category> = PagedDataList(),
    val featuredPlaylists: PagedDataList<Playlist> = PagedDataList(),
    val topTracks: PagedDataList<TopTrack> = PagedDataList(),
    val newReleases: PagedDataList<Album> = PagedDataList()
) : MvRxState
