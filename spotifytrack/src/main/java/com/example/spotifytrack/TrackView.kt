package com.example.spotifytrack

import androidx.databinding.ObservableArrayList
import androidx.databinding.ObservableField
import com.example.coreandroid.model.spotify.Album
import com.example.coreandroid.model.spotify.Artist
import com.example.coreandroid.model.spotify.Track
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