package com.example.coreandroid.base

import androidx.fragment.app.Fragment
import com.example.coreandroid.model.soundcloud.SoundCloudPlaylist
import com.example.coreandroid.model.soundcloud.SoundCloudSystemPlaylist
import com.example.coreandroid.model.soundcloud.SoundCloudTrack
import com.example.coreandroid.model.spotify.*
import com.example.coreandroid.model.videos.VideoPlaylist

interface IFragmentFactory {
    val newSpotifyDashboardNavHostFragment: Fragment
    val newSpotifyAccountNavHostFragment: Fragment
    val newSpotifyFavouritesMainNavHostFragment: Fragment

    fun newSpotifyAlbumFragment(album: Album): Fragment
    fun newSpotifyArtistFragment(artist: Artist): Fragment
    fun newSpotifyCategoryFragment(category: Category): Fragment
    fun newSpotifyPlaylistFragment(playlist: Playlist): Fragment
    fun newSpotifyTrackFragment(track: Track): Fragment
    fun newSpotifyTrackVideosFragment(track: Track): Fragment

    fun newSpotifySearchMainFragment(query: String): Fragment
    fun newSpotifySearchFragment(query: String): Fragment

    fun newVideosSearchFragment(query: String): Fragment
    fun newVideosSearchFragment(videoPlaylist: VideoPlaylist): Fragment
    fun newVideoPlaylistFragment(videoPlaylist: VideoPlaylist, thumbnailUrls: Array<String>): Fragment
    val newVideosFavouritesFragment: Fragment

    val newSoundCloudDashboardNavHostFragment: Fragment
    val newSoundCloudFavouritesNavHostFragment: Fragment

    fun newSoundCloudPlaylistFragmentWithPlaylist(playlist: SoundCloudPlaylist): Fragment
    fun newSoundCloudPlaylistFragmentWithSystemPlaylist(playlist: SoundCloudSystemPlaylist): Fragment
    fun newSoundCloudTrackVideosFragment(track: SoundCloudTrack): Fragment
}