package com.example.core.android.spotify.navigation

import androidx.fragment.app.Fragment
import com.example.core.android.model.spotify.Album
import com.example.core.android.model.spotify.Playlist
import com.example.core.android.model.spotify.Track
import com.example.core.android.spotify.model.Category

interface ISpotifyDashboardNavDestinations {
    fun newSpotifyAlbumFragment(album: Album): Fragment
    fun newSpotifyCategoryFragment(category: Category): Fragment
    fun newSpotifyPlaylistFragment(playlist: Playlist): Fragment
    fun newSpotifyTrackVideosFragment(track: Track): Fragment
}