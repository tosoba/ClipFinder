package com.clipfinder.spotify.track

import com.airbnb.mvrx.MvRxState
import com.clipfinder.core.android.spotify.model.Artist
import com.clipfinder.core.android.spotify.model.Track
import com.clipfinder.core.model.Empty
import com.clipfinder.core.model.Loadable
import com.clipfinder.core.model.PagedList
import com.clipfinder.core.model.Ready
import com.github.mikephil.charting.data.RadarData

data class SpotifyTrackViewState(
    val trackId: Loadable<String>,
    val track: Loadable<Track>,
    val artists: Loadable<List<Artist>>,
    val similarTracks: Loadable<PagedList<Track>>,
    val audioFeaturesChartData: Loadable<RadarData>
) : MvRxState {
    constructor() : this(Empty, Empty, Empty, Empty, Empty)
    constructor(track: Track) : this(Empty, Ready(track), Empty, Empty, Empty)
}
