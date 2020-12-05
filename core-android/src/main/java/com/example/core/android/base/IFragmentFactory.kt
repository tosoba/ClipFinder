package com.example.core.android.base

import androidx.fragment.app.Fragment
import com.example.core.android.model.soundcloud.SoundCloudPlaylist
import com.example.core.android.model.soundcloud.SoundCloudTrack

interface IFragmentFactory {
    val newSpotifyDashboardNavHostFragment: Fragment
    val newSpotifyAccountNavHostFragment: Fragment

    fun newVideosSearchFragment(query: String): Fragment

    val newSoundCloudDashboardNavHostFragment: Fragment

    fun newSoundCloudPlaylistFragmentWithPlaylist(playlist: SoundCloudPlaylist): Fragment
    fun newSoundCloudTrackVideosFragment(track: SoundCloudTrack): Fragment
}
