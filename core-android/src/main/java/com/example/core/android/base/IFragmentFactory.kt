package com.example.core.android.base

import androidx.fragment.app.Fragment
import com.example.core.android.model.soundcloud.SoundCloudPlaylist
import com.example.core.android.model.soundcloud.SoundCloudSystemPlaylist
import com.example.core.android.model.soundcloud.SoundCloudTrack
import com.example.core.android.model.spotify.*
import com.example.core.android.model.videos.VideoPlaylist

interface IFragmentFactory {
    val newSpotifyDashboardNavHostFragment: Fragment
    val newSpotifyAccountNavHostFragment: Fragment

    fun newVideosSearchFragment(query: String): Fragment
    fun newVideosSearchFragment(videoPlaylist: VideoPlaylist): Fragment
    fun newVideoPlaylistFragment(videoPlaylist: VideoPlaylist, thumbnailUrls: Array<String>): Fragment

    val newSoundCloudDashboardNavHostFragment: Fragment

    fun newSoundCloudPlaylistFragmentWithPlaylist(playlist: SoundCloudPlaylist): Fragment
    fun newSoundCloudPlaylistFragmentWithSystemPlaylist(playlist: SoundCloudSystemPlaylist): Fragment
    fun newSoundCloudTrackVideosFragment(track: SoundCloudTrack): Fragment
}
