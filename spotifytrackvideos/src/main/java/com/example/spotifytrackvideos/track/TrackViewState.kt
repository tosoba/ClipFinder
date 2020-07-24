package com.example.spotifytrackvideos.track

import com.airbnb.mvrx.MvRxState
import com.example.core.android.model.Data
import com.example.core.android.model.DataList
import com.example.core.android.model.spotify.Album
import com.example.core.android.model.spotify.Artist
import com.example.core.android.model.spotify.Track
import com.github.mikephil.charting.data.RadarData

data class TrackViewState(
    val album: Data<Album?> = Data(null),
    val artists: DataList<Artist> = DataList(),
    val similarTracks: DataList<Track> = DataList(),
    val audioFeaturesChartData: Data<RadarData?> = Data(null)
) : MvRxState
