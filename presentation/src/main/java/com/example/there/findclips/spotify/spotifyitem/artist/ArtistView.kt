package com.example.there.findclips.spotify.spotifyitem.artist

import android.databinding.ObservableField
import android.databinding.ObservableList
import android.view.View
import com.example.there.findclips.model.entity.spotify.Album
import com.example.there.findclips.model.entity.spotify.Artist
import com.example.there.findclips.model.entity.spotify.Track
import com.example.there.findclips.util.ObservableSortedList

class ArtistView(
        val state: ArtistViewState,
        val onFavouriteBtnClickListener: View.OnClickListener,
        val artistAdapter: ArtistAdapter
)

class ArtistViewState(
        val artist: ObservableField<Artist> = ObservableField(),
        val albums: ObservableList<Album> = ObservableSortedList<Album>(Album::class.java, Album.unsortedListCallback),
        val topTracks: ObservableList<Track> = ObservableSortedList<Track>(Track::class.java, Track.sortedListCallbackName),
        val relatedArtists: ObservableList<Artist> = ObservableSortedList<Artist>(Artist::class.java, Artist.sortedListCallback),
        val albumsLoadingInProgress: ObservableField<Boolean> = ObservableField(false),
        val topTracksLoadingInProgress: ObservableField<Boolean> = ObservableField(false),
        val relatedArtistsLoadingInProgress: ObservableField<Boolean> = ObservableField(false),
        val isSavedAsFavourite: ObservableField<Boolean> = ObservableField(false)
) {
    fun clearAll() {
        albums.clear()
        topTracks.clear()
        relatedArtists.clear()
    }
}