package com.example.there.findclips.fragments.track

import android.databinding.ObservableArrayList
import android.databinding.ObservableField
import android.view.View
import com.example.there.findclips.model.entities.Album
import com.example.there.findclips.model.entities.Artist
import com.example.there.findclips.model.entities.Track
import com.example.there.findclips.view.lists.ArtistsList
import com.example.there.findclips.view.lists.TracksList

data class TrackView(
        val state: TrackViewState,
        val artistsAdapter: ArtistsList.Adapter,
        val similarTracksAdapter: TracksList.Adapter,
        val onAlbumImageViewClickListener: View.OnClickListener
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