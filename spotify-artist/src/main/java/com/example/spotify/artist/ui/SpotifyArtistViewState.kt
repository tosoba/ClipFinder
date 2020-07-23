package com.example.spotify.artist.ui

import com.airbnb.mvrx.MvRxState
import com.example.coreandroid.model.Data
import com.example.coreandroid.model.DataList
import com.example.coreandroid.model.PagedDataList
import com.example.coreandroid.model.spotify.Album
import com.example.coreandroid.model.spotify.Artist
import com.example.coreandroid.model.spotify.Track

data class SpotifyArtistViewState(
    val artists: DataList<Artist> = DataList(),
    val albums: PagedDataList<Album> = PagedDataList(),
    val topTracks: DataList<Track> = DataList(),
    val relatedArtists: DataList<Artist> = DataList(),
    val isSavedAsFavourite: Data<Boolean> = Data(false)
) : MvRxState {
    constructor(argArtist: Artist) : this(DataList(listOf(argArtist)))
}
