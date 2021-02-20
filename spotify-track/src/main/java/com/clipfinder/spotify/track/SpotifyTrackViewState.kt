package com.clipfinder.spotify.track

import com.airbnb.mvrx.MvRxState
import com.clipfinder.core.android.spotify.model.Album
import com.clipfinder.core.android.spotify.model.Artist
import com.clipfinder.core.android.spotify.model.Track
import com.clipfinder.core.model.*
import com.github.mikephil.charting.data.RadarData

data class SpotifyTrackViewState(
    val track: Loadable<Track>,
    val album: Loadable<Album>,
    val artists: Loadable<List<Artist>>,
    val similarTracks: Loadable<PagedList<Track>>,
    val audioFeaturesChartData: Loadable<RadarData>
) : MvRxState {
    constructor() : this(Empty, Empty, Empty, Empty, Empty)
    constructor(track: Track) : this(Ready(track), Empty, Empty, Empty, Empty)
}
