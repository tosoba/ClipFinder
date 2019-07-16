package com.example.coreandroid.base

import com.example.coreandroid.model.soundcloud.SoundCloudPlaylist
import com.example.coreandroid.model.soundcloud.SoundCloudSystemPlaylist
import com.example.coreandroid.model.soundcloud.SoundCloudTrack
import com.example.coreandroid.model.spotify.*
import com.example.coreandroid.model.videos.VideoPlaylist

interface IFragmentFactory {
    val newSpotifyDashboardNavHostFragment: androidx.fragment.app.Fragment
    val newSpotifyAccountNavHostFragment: androidx.fragment.app.Fragment
    val newSpotifyFavouritesMainNavHostFragment: androidx.fragment.app.Fragment

    fun newSpotifyAlbumFragment(album: Album): androidx.fragment.app.Fragment
    fun newSpotifyArtistFragment(artist: Artist): androidx.fragment.app.Fragment
    fun newSpotifyCategoryFragment(category: Category): androidx.fragment.app.Fragment
    fun newSpotifyPlaylistFragment(playlist: Playlist): androidx.fragment.app.Fragment
    fun newSpotifyTrackFragment(track: Track): androidx.fragment.app.Fragment
    fun newSpotifyTrackVideosFragment(track: Track): androidx.fragment.app.Fragment

    fun newSpotifySearchMainFragment(query: String): androidx.fragment.app.Fragment
    fun newSpotifySearchFragment(query: String): androidx.fragment.app.Fragment

    fun newVideosSearchFragment(query: String): androidx.fragment.app.Fragment
    fun newVideosSearchFragment(videoPlaylist: VideoPlaylist): androidx.fragment.app.Fragment
    fun newVideoPlaylistFragment(videoPlaylist: VideoPlaylist, thumbnailUrls: Array<String>): androidx.fragment.app.Fragment
    val newVideosFavouritesFragment: androidx.fragment.app.Fragment

    val newSoundCloudDashboardNavHostFragment: androidx.fragment.app.Fragment
    val newSoundCloudFavouritesNavHostFragment: androidx.fragment.app.Fragment

    fun newSoundCloudPlaylistFragmentWithPlaylist(playlist: SoundCloudPlaylist): androidx.fragment.app.Fragment
    fun newSoundCloudPlaylistFragmentWithSystemPlaylist(playlist: SoundCloudSystemPlaylist): androidx.fragment.app.Fragment
    fun newSoundCloudTrackVideosFragment(track: SoundCloudTrack): androidx.fragment.app.Fragment
}