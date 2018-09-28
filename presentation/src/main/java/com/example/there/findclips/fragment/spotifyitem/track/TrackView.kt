package com.example.there.findclips.fragment.spotifyitem.track

import android.databinding.ObservableArrayList
import android.databinding.ObservableField
import com.example.there.findclips.model.entity.Album
import com.example.there.findclips.model.entity.Artist
import com.example.there.findclips.model.entity.Track

class TrackView(
        val state: TrackViewState,
        val trackAdapter: TrackAdapter
)

data class TrackViewState(
        val albumLoadingInProgress: ObservableField<Boolean> = ObservableField(false),
        val artistsLoadingInProgress: ObservableField<Boolean> = ObservableField(false),
        val similarTracksLoadingInProgress: ObservableField<Boolean> = ObservableField(false),
        val album: ObservableField<Album> = ObservableField(),
        val artists: ObservableArrayList<Artist> = ObservableArrayList(),
        val similarTracks: ObservableArrayList<Track> = ObservableArrayList()
) {
    fun clearAll() {
        similarTracks.clear()
        artists.clear()
    }
}