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
        val albums: ObservableList<Album> = ObservableSortedList<Album>(Album::class.java, object : ObservableSortedList.Callback<Album> {
            override fun compare(o1: Album, o2: Album): Int = o1.name.toLowerCase().compareTo(o2.name.toLowerCase())

            override fun areItemsTheSame(item1: Album, item2: Album): Boolean = item1.id == item2.id

            override fun areContentsTheSame(oldItem: Album, newItem: Album): Boolean = oldItem.id == newItem.id
        }),
        val topTracks: ObservableList<Track> = ObservableSortedList<Track>(Track::class.java, object : ObservableSortedList.Callback<Track> {
            override fun compare(o1: Track, o2: Track): Int = o1.name.toLowerCase().compareTo(o2.name.toLowerCase())

            override fun areItemsTheSame(item1: Track, item2: Track): Boolean = item1.id == item2.id

            override fun areContentsTheSame(oldItem: Track, newItem: Track): Boolean = oldItem.id == newItem.id
        }),
        val relatedArtists: ObservableList<Artist> = ObservableSortedList<Artist>(Artist::class.java, object : ObservableSortedList.Callback<Artist> {
            override fun compare(o1: Artist, o2: Artist): Int = o1.name.toLowerCase().compareTo(o2.name.toLowerCase())

            override fun areItemsTheSame(item1: Artist, item2: Artist): Boolean = item1.id == item2.id

            override fun areContentsTheSame(oldItem: Artist, newItem: Artist): Boolean = oldItem.id == newItem.id
        }),
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