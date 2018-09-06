package com.example.there.findclips.activities.artist

import android.databinding.ObservableField
import android.databinding.ObservableList
import android.view.View
import com.example.there.findclips.model.entities.Album
import com.example.there.findclips.model.entities.Artist
import com.example.there.findclips.model.entities.Track
import com.example.there.findclips.util.ObservableSortedList
import com.example.there.findclips.view.lists.AlbumsList
import com.example.there.findclips.view.lists.ArtistsList
import com.example.there.findclips.view.lists.TracksList

data class ArtistView(
        val state: ArtistViewState,
        val onFavouriteBtnClickListener: View.OnClickListener,
        val albumsAdapter: AlbumsList.Adapter,
        val topTracksAdapter: TracksList.Adapter,
        val relatedArtistsAdapter: ArtistsList.Adapter
)

data class ArtistViewState(
        val artist: ObservableField<Artist> = ObservableField(),
        val albums: ObservableList<Album> = ObservableSortedList<Album>(Album::class.java, Album.sortedListCallback),
        val topTracks: ObservableList<Track> = ObservableSortedList<Track>(Track::class.java, Track.sortedListCallbackName),
        val relatedArtists: ObservableList<Artist> = ObservableSortedList<Artist>(Artist::class.java, Artist.sortedListCallback),
        val albumsLoadingInProgress: ObservableField<Boolean> = ObservableField(false),
        val topTracksLoadingInProgress: ObservableField<Boolean> = ObservableField(false),
        val relatedArtistsLoadingInProgress: ObservableField<Boolean> = ObservableField(false)
) {
    fun clearAll() {
        albums.clear()
        topTracks.clear()
        relatedArtists.clear()
    }
}