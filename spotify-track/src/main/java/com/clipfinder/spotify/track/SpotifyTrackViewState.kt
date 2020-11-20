package com.clipfinder.spotify.track

import com.airbnb.mvrx.MvRxState
import com.example.core.android.model.Empty
import com.example.core.android.model.Loadable
import com.example.core.android.model.PagedItemsList
import com.example.core.android.spotify.model.Album
import com.example.core.android.spotify.model.Artist
import com.example.core.android.spotify.model.Track
import com.github.mikephil.charting.data.RadarData

data class SpotifyTrackViewState(
    val track: Track,
    val album: Loadable<Album>,
    val artists: Loadable<List<Artist>>,
    val similarTracks: Loadable<PagedItemsList<Track>>,
    val audioFeaturesChartData: Loadable<RadarData>
) : MvRxState {
    constructor(track: Track) : this(track, Empty, Empty, Empty, Empty)
}
