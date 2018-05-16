package com.example.there.findclips.activities.artist

import android.databinding.ObservableArrayList
import android.databinding.ObservableField
import android.view.View
import com.example.there.findclips.model.entities.Album
import com.example.there.findclips.model.entities.Artist
import com.example.there.findclips.model.entities.Track
import com.example.there.findclips.view.lists.AlbumsList
import com.example.there.findclips.view.lists.ArtistsList
import com.example.there.findclips.view.lists.TracksList

data class ArtistView(
        val state: ArtistViewState,
        val artist: Artist,
        val onFavouriteBtnClickListener: View.OnClickListener,
        val albumsAdapter: AlbumsList.Adapter,
        val topTracksAdapter: TracksList.Adapter,
        val relatedArtistsAdapter: ArtistsList.Adapter
)

data class ArtistViewState(
        val albums: ObservableArrayList<Album> = ObservableArrayList(),
        val topTracks: ObservableArrayList<Track> = ObservableArrayList(),
        val relatedArtists: ObservableArrayList<Artist> = ObservableArrayList(),
        val albumsLoadingInProgress: ObservableField<Boolean> = ObservableField(false),
        val topTracksLoadingInProgress: ObservableField<Boolean> = ObservableField(false),
        val relatedArtistsLoadingInProgress: ObservableField<Boolean> = ObservableField(false)
)