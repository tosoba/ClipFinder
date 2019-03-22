package com.example.spotifyartist

import android.databinding.ObservableField
import android.databinding.ObservableList
import android.view.View
import com.example.coreandroid.model.spotify.Album
import com.example.coreandroid.model.spotify.Artist
import com.example.coreandroid.model.spotify.Track
import com.example.coreandroid.util.list.IdentifiableNamedObservableListItem
import com.example.coreandroid.util.list.IdentifiableObservableListItem
import com.example.coreandroid.util.list.ObservableSortedList

class ArtistView(
        val state: ArtistViewState,
        val onFavouriteBtnClickListener: View.OnClickListener,
        val artistAdapter: ArtistAdapter
)

class ArtistViewState(
        val artist: ObservableField<Artist> = ObservableField(),
        val albums: ObservableList<Album> = ObservableSortedList<Album>(Album::class.java, IdentifiableObservableListItem.unsortedCallback()),
        val topTracks: ObservableList<Track> = ObservableSortedList<Track>(Track::class.java, IdentifiableNamedObservableListItem.sortedByNameCallback()),
        val relatedArtists: ObservableList<Artist> = ObservableSortedList<Artist>(Artist::class.java, IdentifiableNamedObservableListItem.sortedByNameCallback()),
        val albumsLoadingInProgress: ObservableField<Boolean> = ObservableField(false),
        val topTracksLoadingInProgress: ObservableField<Boolean> = ObservableField(false),
        val relatedArtistsLoadingInProgress: ObservableField<Boolean> = ObservableField(false),
        val albumsLoadingErrorOccurred: ObservableField<Boolean> = ObservableField(false),
        val topTracksLoadingErrorOccurred: ObservableField<Boolean> = ObservableField(false),
        val relatedArtistsLoadingErrorOccurred: ObservableField<Boolean> = ObservableField(false),
        val isSavedAsFavourite: ObservableField<Boolean> = ObservableField(false)
) {
    fun clearAll() {
        albums.clear()
        topTracks.clear()
        relatedArtists.clear()
    }
}