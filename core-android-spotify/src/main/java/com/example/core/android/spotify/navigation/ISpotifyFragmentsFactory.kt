package com.example.core.android.spotify.navigation

import androidx.fragment.app.Fragment
import com.example.core.android.model.spotify.Album
import com.example.core.android.model.spotify.Artist
import com.example.core.android.model.spotify.Track
import com.example.core.android.spotify.model.Category
import com.example.core.android.spotify.model.Playlist

interface ISpotifyFragmentsFactory {
    fun newSpotifyAlbumFragment(album: Album): Fragment
    fun newSpotifyArtistFragment(artist: Artist): Fragment
    fun newSpotifyPlaylistFragment(playlist: Playlist): Fragment
    fun newSpotifyTrackVideosFragment(track: Track): Fragment
    fun newSpotifyCategoryFragment(category: Category): Fragment
    fun newSpotifySearchMainFragment(query: String): Fragment
    fun newSpotifySearchFragment(query: String): Fragment
}