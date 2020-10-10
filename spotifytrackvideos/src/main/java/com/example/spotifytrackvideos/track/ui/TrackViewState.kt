package com.example.spotifytrackvideos.track.ui

import com.airbnb.mvrx.MvRxState
import com.example.core.android.model.Data
import com.example.core.android.model.DataList
import com.example.core.android.model.PagedDataList
import com.example.core.android.spotify.model.Album
import com.example.core.android.spotify.model.Artist
import com.example.core.android.spotify.model.Track
import com.github.mikephil.charting.data.RadarData

data class TrackViewState(
    val album: Data<Album?> = Data(null),
    val artists: DataList<Artist> = DataList(),
    val similarTracks: PagedDataList<Track> = PagedDataList(),
    val audioFeaturesChartData: Data<RadarData?> = Data(null)
) : MvRxState
