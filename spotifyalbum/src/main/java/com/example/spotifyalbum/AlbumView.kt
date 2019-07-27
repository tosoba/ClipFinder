package com.example.spotifyalbum

import android.view.View
import androidx.databinding.ObservableField
import androidx.databinding.ObservableList
import com.example.coreandroid.model.spotify.Album
import com.example.coreandroid.model.spotify.Artist
import com.example.coreandroid.model.spotify.Track
import com.example.coreandroid.util.list.IdentifiableNamedObservableListItem
import com.example.coreandroid.util.list.IdentifiableNumberedObservableListItem
import com.example.coreandroid.util.list.ObservableSortedList
import com.example.coreandroid.view.recyclerview.adapter.ArtistsAndTracksAdapter

class AlbumView(
        val state: AlbumViewState,
        val album: Album,
        val onFavouriteBtnClickListener: View.OnClickListener,
        val artistsAndTracksAdapter: ArtistsAndTracksAdapter
)

class AlbumViewState(
        val artists: ObservableList<Artist> = ObservableSortedList<Artist>(Artist::class.java, IdentifiableNamedObservableListItem.sortedByNameCallback()),
        val tracks: ObservableList<Track> = ObservableSortedList<Track>(Track::class.java, IdentifiableNumberedObservableListItem.sortedByNumberCallback()),
        val artistsLoadingInProgress: ObservableField<Boolean> = ObservableField(false),
        val tracksLoadingInProgress: ObservableField<Boolean> = ObservableField(false),
        val artistsLoadingErrorOccurred: ObservableField<Boolean> = ObservableField(false),
        val tracksLoadingErrorOccurred: ObservableField<Boolean> = ObservableField(false),
        val isSavedAsFavourite: ObservableField<Boolean> = ObservableField(false)
)