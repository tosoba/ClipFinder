package com.example.there.findclips.search.spotify

import android.databinding.ObservableArrayList
import android.databinding.ObservableField
import com.example.there.domain.entities.spotify.AlbumEntity
import com.example.there.domain.entities.spotify.ArtistEntity
import com.example.there.domain.entities.spotify.PlaylistEntity
import com.example.there.domain.entities.spotify.TrackEntity

data class SpotifySearchViewState(
        val albums: ObservableArrayList<AlbumEntity> = ObservableArrayList(),
        val artists: ObservableArrayList<ArtistEntity> = ObservableArrayList(),
        val playlists: ObservableArrayList<PlaylistEntity> = ObservableArrayList(),
        val tracks: ObservableArrayList<TrackEntity> = ObservableArrayList(),
        val loadingInProgress: ObservableField<Boolean> = ObservableField(false)
) {
    fun addAlbumsSorted(newAlbums: List<AlbumEntity>) {
        albums.addAll(newAlbums.sortedBy { it.name })
    }

    fun addArtistsSorted(newArtists: List<ArtistEntity>) {
        artists.addAll(newArtists.sortedBy { it.name })
    }

    fun addPlaylistsSorted(newPlaylists: List<PlaylistEntity>) {
        playlists.addAll(newPlaylists.sortedBy { it.name })
    }

    fun addTracksSorted(newTracks: List<TrackEntity>) {
        tracks.addAll(newTracks.sortedBy { it.name })
    }

    fun clearAll() {
        albums.clear()
        artists.clear()
        playlists.clear()
        tracks.clear()
    }
}