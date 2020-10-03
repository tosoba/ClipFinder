package com.example.spotify.artist.ui

import com.airbnb.mvrx.MvRxState
import com.example.core.android.model.Data
import com.example.core.android.model.DataList
import com.example.core.android.model.PagedDataList
import com.example.core.android.spotify.model.Album
import com.example.core.android.spotify.model.Artist
import com.example.core.android.spotify.model.Track

data class SpotifyArtistViewState(
    val artists: DataList<Artist> = DataList(),
    val albums: PagedDataList<Album> = PagedDataList(),
    val topTracks: DataList<Track> = DataList(),
    val relatedArtists: DataList<Artist> = DataList(),
    val isSavedAsFavourite: Data<Boolean> = Data(false)
) : MvRxState {
    constructor(argArtist: Artist) : this(DataList(listOf(argArtist)))
}
