package com.clipfinder.spotify.track

import com.airbnb.mvrx.MvRxState
import com.example.core.android.model.*
import com.example.core.android.spotify.model.Album
import com.example.core.android.spotify.model.Artist
import com.example.core.android.spotify.model.Track
import com.github.mikephil.charting.data.RadarData

data class SpotifyTrackViewState(
    val track: Track,
    val album: Loadable<Album> = Empty,
    val artists: DefaultLoadable<List<Artist>> = DefaultReady(emptyList()),
    val similarTracks: DefaultLoadable<PagedItemsList<Track>> = DefaultReady(PagedItemsList()),
    val audioFeaturesChartData: Loadable<RadarData> = Empty
) : MvRxState {
    constructor(track: Track) : this(
        track,
        Empty,
        DefaultReady(emptyList()),
        DefaultReady(PagedItemsList()),
        Empty
    )
}
