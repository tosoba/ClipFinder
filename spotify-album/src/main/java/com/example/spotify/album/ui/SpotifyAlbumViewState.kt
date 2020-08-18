package com.example.spotify.album.ui

import com.airbnb.mvrx.MvRxState
import com.example.core.android.model.Data
import com.example.core.android.model.DataList
import com.example.core.android.model.PagedDataList
import com.example.core.android.model.spotify.Album
import com.example.core.android.model.spotify.Artist
import com.example.core.android.model.spotify.Track

data class SpotifyAlbumViewState(
    val album: Album,
    val artists: DataList<Artist> = DataList(),
    val tracks: PagedDataList<Track> = PagedDataList(),
    val isSavedAsFavourite: Data<Boolean> = Data(false)
) : MvRxState {
    constructor(argAlbum: Album) : this(argAlbum, DataList(), PagedDataList(), Data(false))
}