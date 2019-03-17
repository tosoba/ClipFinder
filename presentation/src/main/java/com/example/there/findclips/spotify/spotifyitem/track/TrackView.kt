package com.example.there.findclips.spotify.spotifyitem.track

import android.databinding.ObservableArrayList
import android.databinding.ObservableField
import com.github.mikephil.charting.data.RadarData

class TrackView(
        val state: TrackViewState,
        val trackAdapter: TrackAdapter
)

class TrackViewState(
        val albumLoadingInProgress: ObservableField<Boolean> = ObservableField(false),
        val artistsLoadingInProgress: ObservableField<Boolean> = ObservableField(false),
        val similarTracksLoadingInProgress: ObservableField<Boolean> = ObservableField(false),
        val album: ObservableField<Album> = ObservableField(),
        val artists: ObservableArrayList<Artist> = ObservableArrayList(),
        val similarTracks: ObservableArrayList<Track> = ObservableArrayList(),
        val artistsLoadingErrorOccurred: ObservableField<Boolean> = ObservableField(false),
        val similarTracksErrorOccurred: ObservableField<Boolean> = ObservableField(false),
        val audioFeaturesChartData: ObservableField<RadarData> = ObservableField()
) {
    fun clearAll() {
        similarTracks.clear()
        artists.clear()
        similarTracksErrorOccurred.set(false)
        artistsLoadingErrorOccurred.set(false)
    }
}