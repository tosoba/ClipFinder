package com.example.there.findclips.spotify.spotifyitem.track

import android.databinding.ObservableArrayList
import android.databinding.ObservableField
import com.example.there.findclips.model.entity.spotify.Album
import com.example.there.findclips.model.entity.spotify.Artist
import com.example.there.findclips.model.entity.spotify.Track
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
        val audioFeaturesChartData: ObservableField<RadarData> = ObservableField()
) {
    fun clearAll() {
        similarTracks.clear()
        artists.clear()
    }
}