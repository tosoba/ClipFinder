package com.example.there.findclips.activities.album

import android.databinding.ObservableField
import android.databinding.ObservableList
import android.support.v7.widget.RecyclerView
import android.view.View
import com.example.there.findclips.model.entities.Album
import com.example.there.findclips.model.entities.Artist
import com.example.there.findclips.model.entities.Track
import com.example.there.findclips.util.ObservableSortedList
import com.example.there.findclips.view.lists.ArtistsList
import com.example.there.findclips.view.lists.TracksPopularityList
import com.example.there.findclips.view.recycler.SeparatorDecoration

data class AlbumView(
        val state: AlbumViewState,
        val album: Album,
        val onFavouriteBtnClickListener: View.OnClickListener,
        val artistsAdapter: ArtistsList.Adapter,
        val tracksAdapter: TracksPopularityList.Adapter,
        val separatorDecoration: SeparatorDecoration,
        val onTracksScrollListener: RecyclerView.OnScrollListener
)

data class AlbumViewState(
        val artists: ObservableList<Artist> = ObservableSortedList<Artist>(Artist::class.java, object : ObservableSortedList.Callback<Artist> {
            override fun compare(o1: Artist, o2: Artist): Int = o1.name.toLowerCase().compareTo(o2.name.toLowerCase())

            override fun areItemsTheSame(item1: Artist, item2: Artist): Boolean = item1.id == item2.id

            override fun areContentsTheSame(oldItem: Artist, newItem: Artist): Boolean = oldItem.id == newItem.id
        }),
        val tracks: ObservableList<Track> = ObservableSortedList<Track>(Track::class.java, object : ObservableSortedList.Callback<Track> {
            override fun compare(o1: Track, o2: Track): Int = o1.trackNumber.compareTo(o2.trackNumber)

            override fun areItemsTheSame(item1: Track, item2: Track): Boolean = item1.id == item2.id

            override fun areContentsTheSame(oldItem: Track, newItem: Track): Boolean = oldItem.id == newItem.id
        }),
        val artistsLoadingInProgress: ObservableField<Boolean> = ObservableField(false),
        val tracksLoadingInProgress: ObservableField<Boolean> = ObservableField(false)
)