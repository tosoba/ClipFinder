package com.example.there.findclips.spotify.spotifyitem.album

import android.databinding.ObservableField
import android.databinding.ObservableList
import android.view.View

class AlbumView(
        val state: AlbumViewState,
        val album: Album,
        val onFavouriteBtnClickListener: View.OnClickListener,
        val onPlayBtnClickListener: View.OnClickListener,
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