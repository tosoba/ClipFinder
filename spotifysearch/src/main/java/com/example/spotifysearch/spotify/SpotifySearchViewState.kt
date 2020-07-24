package com.example.spotifysearch.spotify

import com.airbnb.mvrx.MvRxState
import com.example.core.android.model.DataStatus
import com.example.core.android.model.Initial
import com.example.core.android.model.spotify.Album
import com.example.core.android.model.spotify.Artist
import com.example.core.android.model.spotify.Playlist
import com.example.core.android.model.spotify.Track


data class SpotifySearchViewState(
    val query: String,
    val offset: Int = 0,
    val totalItems: Int = 0,
    val status: DataStatus = Initial,
    val albums: SpotifySearchItemList<Album> = SpotifySearchItemList(status = status),
    val artists: SpotifySearchItemList<Artist> = SpotifySearchItemList(status = status),
    val playlists: SpotifySearchItemList<Playlist> = SpotifySearchItemList(status = status),
    val tracks: SpotifySearchItemList<Track> = SpotifySearchItemList(status = status)
) : MvRxState {
    constructor(query: String) : this(
        query,
        0,
        0,
        Initial,
        SpotifySearchItemList(status = Initial),
        SpotifySearchItemList(status = Initial),
        SpotifySearchItemList(status = Initial),
        SpotifySearchItemList(status = Initial)
    )

    fun resetWithNewQuery(query: String): SpotifySearchViewState = SpotifySearchViewState(query)
}
