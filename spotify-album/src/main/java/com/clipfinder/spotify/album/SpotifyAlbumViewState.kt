package com.clipfinder.spotify.album

import com.airbnb.mvrx.MvRxState
import com.example.core.android.model.DataList
import com.example.core.android.model.PagedDataList
import com.example.core.android.spotify.model.Album
import com.example.core.android.spotify.model.Artist
import com.example.core.android.spotify.model.Track

data class SpotifyAlbumViewState(
    val album: Album,
    val artists: DataList<Artist> = DataList(),
    val tracks: PagedDataList<Track> = PagedDataList()
) : MvRxState {
    constructor(argAlbum: Album) : this(argAlbum, DataList(), PagedDataList())
}
