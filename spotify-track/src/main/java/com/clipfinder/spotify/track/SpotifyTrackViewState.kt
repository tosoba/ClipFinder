package com.clipfinder.spotify.track

import com.airbnb.mvrx.MvRxState
import com.clipfinder.core.model.Empty
import com.clipfinder.core.model.Loadable
import com.clipfinder.core.model.PagedList
import com.example.core.android.spotify.model.Album
import com.example.core.android.spotify.model.Artist
import com.example.core.android.spotify.model.Track
import com.github.mikephil.charting.data.RadarData

data class SpotifyTrackViewState(
    val track: Track,
    val album: Loadable<Album>,
    val artists: Loadable<List<Artist>>,
    val similarTracks: Loadable<PagedList<Track>>,
    val audioFeaturesChartData: Loadable<RadarData>
) : MvRxState {
    constructor(track: Track) : this(track, Empty, Empty, Empty, Empty)
}
