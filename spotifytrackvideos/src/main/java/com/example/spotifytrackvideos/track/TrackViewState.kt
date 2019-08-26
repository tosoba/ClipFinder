package com.example.spotifytrackvideos.track

import com.airbnb.mvrx.MvRxState
import com.example.coreandroid.model.Data
import com.example.coreandroid.model.DataList
import com.example.coreandroid.model.spotify.Album
import com.example.coreandroid.model.spotify.Artist
import com.example.coreandroid.model.spotify.Track
import com.github.mikephil.charting.data.RadarData


data class TrackViewState(
        val album: Data<Album?> = Data(null),
        val artists: DataList<Artist> = DataList(),
        val similarTracks: DataList<Track> = DataList(),
        val audioFeaturesChartData: Data<RadarData?> = Data(null)
) : MvRxState