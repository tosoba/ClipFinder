package com.clipfinder.spotify.search

import com.airbnb.mvrx.MvRxState
import com.example.core.android.model.PagedDataList
import com.example.core.android.spotify.model.Album
import com.example.core.android.spotify.model.Artist
import com.example.core.android.spotify.model.Playlist
import com.example.core.android.spotify.model.Track

data class SpotifySearchViewState(
    val query: String,
    val albums: PagedDataList<Album> = PagedDataList(),
    val artists: PagedDataList<Artist> = PagedDataList(),
    val playlists: PagedDataList<Playlist> = PagedDataList(),
    val tracks: PagedDataList<Track> = PagedDataList()
) : MvRxState {
    constructor(query: String) : this(
        query, PagedDataList(), PagedDataList(), PagedDataList(), PagedDataList()
    )
}
