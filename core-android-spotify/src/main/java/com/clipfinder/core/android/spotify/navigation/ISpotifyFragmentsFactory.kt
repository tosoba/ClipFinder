package com.clipfinder.core.android.spotify.navigation

import androidx.fragment.app.Fragment
import com.clipfinder.core.android.spotify.model.*

interface ISpotifyFragmentsFactory {
    fun newSpotifyAlbumFragment(album: Album): Fragment
    fun newSpotifyArtistFragment(artist: Artist): Fragment
    fun newSpotifyPlaylistFragment(playlist: Playlist): Fragment
    fun newSpotifyTrackVideosFragment(track: Track): Fragment
    fun newSpotifyCategoryFragment(category: Category): Fragment
    fun newSpotifySearchMainFragment(query: String): Fragment
    fun newSpotifySearchFragment(query: String): Fragment
    fun newSpotifyTrackFragment(track: Track): Fragment
}
