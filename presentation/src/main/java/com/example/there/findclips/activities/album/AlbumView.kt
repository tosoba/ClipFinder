package com.example.there.findclips.activities.album

import android.databinding.ObservableArrayList
import android.databinding.ObservableField
import android.view.View
import com.example.there.findclips.model.entities.Artist
import com.example.there.findclips.model.entities.Track
import com.example.there.findclips.model.entities.Album
import com.example.there.findclips.view.lists.ArtistsList

data class AlbumView(val state: AlbumViewState,
                     val album: Album,
                     val onFavouriteBtnClickListener: View.OnClickListener,
                     val artistsAdapter: ArtistsList.Adapter)

data class AlbumViewState(val artists: ObservableArrayList<Artist> = ObservableArrayList(),
                          val tracks: ObservableArrayList<Track> = ObservableArrayList(),
                          val artistsLoadingInProgress: ObservableField<Boolean> = ObservableField(false),
                          val tracksLoadingInProgress: ObservableField<Boolean> = ObservableField(false))