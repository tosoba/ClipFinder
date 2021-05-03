package com.clipfinder.spotify.artist

import com.airbnb.mvrx.MvRxState
import com.clipfinder.core.android.spotify.model.Album
import com.clipfinder.core.android.spotify.model.Artist
import com.clipfinder.core.android.spotify.model.Track
import com.clipfinder.core.model.Loadable
import com.clipfinder.core.model.PagedList
import com.clipfinder.core.model.Ready

data class SpotifyArtistViewState(
    val artists: List<Artist> = emptyList(),
    val albums: Loadable<PagedList<Album>> = Ready(PagedList()),
    val topTracks: Loadable<List<Track>> = Ready(emptyList()),
    val relatedArtists: Loadable<List<Artist>> = Ready(emptyList()),
    val isSavedAsFavourite: Loadable<Boolean> = Ready(false)
) : MvRxState {
    constructor(argArtist: Artist) : this(listOf(argArtist))
}
