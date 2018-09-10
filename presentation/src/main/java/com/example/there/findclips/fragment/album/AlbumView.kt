package com.example.there.findclips.fragment.album

import android.databinding.ObservableField
import android.databinding.ObservableList
import android.view.View
import com.example.there.findclips.model.entity.Album
import com.example.there.findclips.model.entity.Artist
import com.example.there.findclips.model.entity.Track
import com.example.there.findclips.util.ObservableSortedList

class AlbumView(
        val state: AlbumViewState,
        val album: Album,
        val onFavouriteBtnClickListener: View.OnClickListener,
        val albumAdapter: AlbumAdapter
)

data class AlbumViewState(
        val artists: ObservableList<Artist> = ObservableSortedList<Artist>(Artist::class.java, Artist.sortedListCallback),
        val tracks: ObservableList<Track> = ObservableSortedList<Track>(Track::class.java, Track.sortedListCallbackTrackNumber),
        val artistsLoadingInProgress: ObservableField<Boolean> = ObservableField(false),
        val tracksLoadingInProgress: ObservableField<Boolean> = ObservableField(false)
)