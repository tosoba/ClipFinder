package com.example.there.findclips.track

import android.databinding.ObservableArrayList
import android.databinding.ObservableField
import com.example.there.findclips.entities.Album
import com.example.there.findclips.entities.Artist
import com.example.there.findclips.entities.Track
import com.example.there.findclips.lists.ArtistsList
import com.example.there.findclips.lists.SimilarTracksList

data class TrackView(
        val state: TrackViewState,
        val artistsAdapter: ArtistsList.Adapter,
        val similarTracksAdapter: SimilarTracksList.Adapter
)

data class TrackViewState(
        val albumLoadingInProgress: ObservableField<Boolean> = ObservableField(false),
        val artistsLoadingInProgress: ObservableField<Boolean> = ObservableField(false),
        val similarTracksLoadingInProgress: ObservableField<Boolean> = ObservableField(false),
        val album: ObservableField<Album> = ObservableField(),
        val artists: ObservableArrayList<Artist> = ObservableArrayList(),
        val similarTracks: ObservableArrayList<Track> = ObservableArrayList()
)