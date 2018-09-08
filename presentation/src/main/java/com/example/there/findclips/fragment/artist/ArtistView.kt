package com.example.there.findclips.fragment.artist

import android.databinding.ObservableField
import android.databinding.ObservableList
import android.view.View
import com.example.there.findclips.model.entity.Album
import com.example.there.findclips.model.entity.Artist
import com.example.there.findclips.model.entity.Track
import com.example.there.findclips.util.ObservableSortedList
import com.example.there.findclips.view.list.AlbumsList
import com.example.there.findclips.view.list.ArtistsList
import com.example.there.findclips.view.list.TracksList

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