package com.example.spotifyartist

import com.airbnb.mvrx.MvRxState
import com.example.coreandroid.model.Data
import com.example.coreandroid.model.DataList
import com.example.coreandroid.model.spotify.Album
import com.example.coreandroid.model.spotify.Artist
import com.example.coreandroid.model.spotify.Track

data class ArtistViewState(
        val artists: DataList<Artist> = DataList(),
        val albums: DataList<Album> = DataList(),
        val topTracks: DataList<Track> = DataList(),
        val relatedArtists: DataList<Artist> = DataList(),
        val isSavedAsFavourite: Data<Boolean> = Data(false)
) : MvRxState {
    constructor(argArtist: Artist) : this(DataList(listOf(argArtist)))
}