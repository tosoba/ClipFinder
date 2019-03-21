package com.example.coreandroid.base

import android.support.v4.app.Fragment
import com.example.coreandroid.model.soundcloud.SoundCloudPlaylist
import com.example.coreandroid.model.soundcloud.SoundCloudSystemPlaylist
import com.example.coreandroid.model.spotify.*
import com.example.coreandroid.model.videos.VideoPlaylist

interface IFragmentFactory {
    fun newSpotifyAlbumFragment(album: Album): Fragment
    fun newSpotifyArtistFragment(artist: Artist): Fragment
    fun newSpotifyCategoryFragment(category: Category): Fragment
    fun newSpotifyPlaylistFragment(playlist: Playlist): Fragment
    fun newSpotifyTrackFragment(track: Track): Fragment
    fun newSpotifyTrackVideosFragment(track: Track): Fragment

    fun newSpotifySearchFragment(query: String): Fragment

    fun newVideoPlaylistFragment(videoPlaylist: VideoPlaylist, thumbnailUrls: Array<String>): Fragment
    val newVideosFavouritesFragment: Fragment

    fun newSoundCloudPlaylistFragmentWithPlaylist(playlist: SoundCloudPlaylist): Fragment
    fun newSoundCloudPlaylistFragmentWithSystemPlaylist(playlist: SoundCloudSystemPlaylist): Fragment
}