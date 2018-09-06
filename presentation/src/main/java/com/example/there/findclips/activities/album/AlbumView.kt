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
        val artists: ObservableList<Artist> = ObservableSortedList<Artist>(Artist::class.java, Artist.sortedListCallback),
        val tracks: ObservableList<Track> = ObservableSortedList<Track>(Track::class.java, Track.sortedListCallbackTrackNumber),
        val artistsLoadingInProgress: ObservableField<Boolean> = ObservableField(false),
        val tracksLoadingInProgress: ObservableField<Boolean> = ObservableField(false)
)