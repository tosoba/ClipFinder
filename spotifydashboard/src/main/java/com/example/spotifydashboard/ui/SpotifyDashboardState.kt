package com.example.spotifydashboard.ui

import com.airbnb.mvrx.MvRxState
import com.example.coreandroid.model.DataList
import com.example.coreandroid.model.PagedDataList
import com.example.coreandroid.model.spotify.Album
import com.example.coreandroid.model.spotify.Category
import com.example.coreandroid.model.spotify.Playlist
import com.example.coreandroid.model.spotify.TopTrack

data class SpotifyDashboardState(
    val categories: PagedDataList<Category> = PagedDataList(),
    val featuredPlaylists: PagedDataList<Playlist> = PagedDataList(),
    val topTracks: DataList<TopTrack> = DataList(),
    val newReleases: PagedDataList<Album> = PagedDataList()
) : MvRxState
